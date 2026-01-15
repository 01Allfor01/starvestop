package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public CreateSubscriptionResponse createSubscription(Long storeId, Long ownerId, CreateSubscriptionRequest request) {

        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );

        if (!Objects.equals(ownerId, store.getUser().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        Subscription subscription = Subscription.create(store, request.getSubscriptionName(), request.getDescription(), request.getPrice());

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return CreateSubscriptionResponse.from(savedSubscription);
    }
}
