package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
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
    private final StoreRepository storeRepository;

    @Transactional
    public CreateSubscriptionResponse createSubscription(AuthUser authUser, Long storeId, @Valid CreateSubscriptionRequest request) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );

        Long ownerId = authUser.getUserId();
        if (!ownerId.equals(store.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Subscription subscription = Subscription.create(
                store,
                request.getSubscriptionName(),
                request.getDescription(),
                request.getDay(),
                request.getMealTime(),
                request.getPrice(),
                request.getStock()
        );

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return CreateSubscriptionResponse.from(savedSubscription);
    }

    @Transactional(readOnly = true)
    public List<GetSubscriptionResponse> getSubscriptionList() {

        List<Subscription> subscriptionList = subscriptionRepository.findAllByIsDeletedIsFalse();
        return subscriptionList.stream().map(GetSubscriptionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetSubscriptionResponse getSubscription(Long subscriptionId) {

        Subscription subscription = getSubscriptionOrThrow(subscriptionId);
        return GetSubscriptionResponse.from(subscription);
    }

    @Transactional
    public void deleteSubscription(AuthUser authUser, Long subscriptionId) {

        Subscription subscription = getSubscriptionOrThrow(subscriptionId);
        checkPermission(authUser, subscription);

        subscription.delete();
    }

    @Transactional(readOnly = true)
    public List<GetSubscriptionResponse> getSubscriptionListByStore(Long storeId) {
        List<Subscription> subscriptionList = subscriptionRepository.findByStoreIdAndIsDeletedIsFalse(storeId);
        return subscriptionList.stream().map(GetSubscriptionResponse::from).toList();
    }

    @Transactional
    public UpdateSubscriptionResponse updateSubscription(UpdateSubscriptionRequest request, Long subscriptionId) {
        Subscription subscription = getSubscriptionOrThrow(subscriptionId);
        subscription.changeIsJoinable(request.isJoinable());
        subscriptionRepository.flush();
        return UpdateSubscriptionResponse.from(subscription);
    }

    private static void checkPermission(AuthUser authUser, Subscription subscription) {

        Long ownerId = subscription.getStore().getUser().getId();
        if (UserRole.ADMIN == authUser.getUserRole()) {
            return;
        }

        if (UserRole.OWNER == authUser.getUserRole() && ownerId.equals(authUser.getUserId())) {
            return;
        }

        throw new CustomException(ErrorCode.FORBIDDEN);
    }

    private Subscription getSubscriptionOrThrow(Long subscriptionId) {

        return subscriptionRepository.findByIdAndIsDeletedIsFalse(subscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND)
        );
    }
}

