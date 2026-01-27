package com.allforone.starvestop.domain.subscription;

import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.repository.OwnerRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.subscription.service.SubscriptionFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
@Tag("concurrency")
@ActiveProfiles("test")
public class SubscriptionConCurrencyTest {

    @Autowired
    private SubscriptionFunction subscriptionFunction;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private StoreRepository storeRepository;

    private Store store;

    @BeforeEach
    void setUp() {
        Point point = GeometryUtil.createPoint(127.0, 37.0);
        Owner owner = Owner.create("test6@test.com", "password", "lee");
        ownerRepository.save(owner);

        store = Store.create(owner, "판교 국밥", "경기도 성남시 분당구 판교역로 123", "뜨끈한 국밥집",
                StoreCategory.KOREAN_FOOD, point, LocalTime.now(), LocalTime.now().plusHours(9),
                StoreStatus.OPENED, "사업자 등록 번호");
        storeRepository.save(store);
    }

    @Test
    @DisplayName("동시에_5명이_재고_1개를_구독하기")
    void concurrencyTest_subscription_Success() throws InterruptedException {

        //given
        Subscription subscription = Subscription.create(store, "판교 국밥 화목 저녁 구독", "화·목 퇴근 후 즐기는 국밥 정기 구독",
                10, 4, new BigDecimal(9200),  1);

        Subscription saved = subscriptionRepository.save(subscription);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        int thread = 5;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(thread);

        //when
        ExecutorService executor = Executors.newFixedThreadPool(thread);

        for(int i = 0; i < thread; i++) {
            executor.submit(() -> {
                try {
                    start.await();
                    subscriptionFunction.decreaseById(saved.getId(), 1);
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " 실패: " + e.getMessage());
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await();
        executor.shutdown();    //환경을 이제 동시에 시작하도록 준비 시작

        //then
        Subscription result = subscriptionRepository.findById(saved.getId()).orElseThrow();

        System.out.println("최종 재고 : " + result.getStock());
        System.out.println("차감 성공 횟수 : " + success);
        System.out.println("차감 실패 횟수 : " + fail);
    }
}
