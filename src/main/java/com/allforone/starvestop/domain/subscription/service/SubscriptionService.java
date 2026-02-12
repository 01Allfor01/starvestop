package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.request.UpdateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.UpdateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.user.enums.UserRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final StoreService storeService;

    // 구독 생성
    @Transactional
    public CreateSubscriptionResponse createSubscription(AuthUser authUser, Long storeId, @Valid CreateSubscriptionRequest request) {
        Store store = storeService.getById(storeId);

        Long ownerId = authUser.getUserId();
        if (!ownerId.equals(store.getOwner().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Subscription subscription = Subscription.create(
                store,
                request.getName(),
                request.getDescription(),
                request.getDay(),
                request.getMealTime(),
                request.getPrice(),
                request.getStock()
        );

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return CreateSubscriptionResponse.from(savedSubscription);
    }

    // 전체 구독 목록 조회
    @Transactional(readOnly = true)
    public List<GetSubscriptionResponse> getSubscriptionList() {
        List<Subscription> subscriptionList = subscriptionRepository.findAllByIsDeletedIsFalse();

        return subscriptionList.stream().map(GetSubscriptionResponse::from).toList();
    }


    // 특정 매장 구독 목록 조회
    @Transactional(readOnly = true)
    public List<GetSubscriptionResponse> getSubscriptionListByStore(Long storeId) {
        List<Subscription> subscriptionList = subscriptionRepository.findByStoreIdAndIsDeletedIsFalse(storeId);

        return subscriptionList.stream().map(GetSubscriptionResponse::from).toList();
    }

    // 구독 상세 조회
    @Transactional(readOnly = true)
    public GetSubscriptionResponse getSubscription(Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);

        return GetSubscriptionResponse.from(subscription);
    }

    // 구독 수정
    @Transactional
    public UpdateSubscriptionResponse updateSubscription(UpdateSubscriptionRequest request, Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);

        subscription.changeIsJoinable(request.getJoinable());

        subscriptionRepository.flush();

        return UpdateSubscriptionResponse.from(subscription);
    }

    // 구독 삭제
    @Transactional
    public void deleteSubscription(AuthUser authUser, Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);

        checkPermission(authUser, subscription);

        subscription.delete();
    }

    //권한 확인
    @Transactional
    public void checkPermission(AuthUser authUser, Subscription subscription) {
        Long ownerId = subscription.getStore().getOwner().getId();

        if (UserRole.ADMIN == authUser.getUserRole()) {
            return;
        }

        if (UserRole.OWNER == authUser.getUserRole() && ownerId.equals(authUser.getUserId())) {
            return;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    //구독 조회
    @Transactional
    public Subscription getSubscriptionOrThrow(Long id) {
        return subscriptionRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    //구독 재고 차감
    @Transactional
    public void decreaseById(Long id) {
        int change = subscriptionRepository.decreaseStock(id);

        if (change == 0) {
            throw new CustomException(ErrorCode.SUBSCRIPTION_OUT_OF_STOCK);
        }
    }

    //구독 재고 증가
    @Transactional
    public void increaseById(Long id, Integer count) {
        int change = subscriptionRepository.increaseStock(id);
    }
}

