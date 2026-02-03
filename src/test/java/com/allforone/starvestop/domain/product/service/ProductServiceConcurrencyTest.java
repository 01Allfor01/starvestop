package com.allforone.starvestop.domain.product.service;

import com.allforone.starvestop.common.utils.GeometryUtil;
import com.allforone.starvestop.domain.owner.entity.Owner;
import com.allforone.starvestop.domain.owner.repository.OwnerRepository;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.enums.StoreCategory;
import com.allforone.starvestop.domain.store.enums.StoreStatus;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest

class ProductServiceConcurrencyTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    private Store store;

    @BeforeEach
    void setUp() {
        Owner owner = Owner.create("lee@seo.jun", "password", "leeseojun");
        ownerRepository.save(owner);

        Point point = GeometryUtil.createPoint(127.001695, 37.5642135);

        store = Store.create(owner, "올포원 가게", "내배캠", "이곳은 내배캠", StoreCategory.KOREAN_FOOD,
                point, LocalTime.now(), LocalTime.now().plusHours(9), StoreStatus.OPENED, "사업자 등록 번호");
        storeRepository.save(store);

    }

    @Test
    @DisplayName("재고감소_동시성_테스트")
    void decreaseById() throws InterruptedException {
        //given
        Product product = Product.create(store, "민트초코치즈케이크", "맛있는지는 모르겠음", new BigDecimal(8000), new BigDecimal(7000), 10, ProductStatus.GENERAL);
        Product savedProduct = productRepository.save(product);

        int thread = 100;

        CountDownLatch ready = new CountDownLatch(thread);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(thread);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        ExecutorService executorService = Executors.newFixedThreadPool(thread);

        //when
        for (int i = 0; i < thread; ++i) {
            executorService.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    productService.decreaseById(savedProduct.getId(), 2);
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        long startTime = System.nanoTime();
        start.countDown();
        done.await();
        long endTime = System.nanoTime();
        executorService.shutdown();

        //then
        Product result = productService.getProduct(savedProduct.getId());

        System.out.printf("남은 재고 : %d\n", result.getStock());
        System.out.printf("차감 성공 횟수 : %d\n", success.get());
        System.out.printf("차감 실패 횟수 : %d\n", fail.get());
        System.out.printf("동시성 테스트 실행 구간 소요시간 : %d\n", (endTime - startTime) / 1_000);


        assertThat(success.get()).isEqualTo(5);
        assertThat(fail.get()).isEqualTo(95);

        assertThat(result.getStock()).isEqualTo(0);
    }
}