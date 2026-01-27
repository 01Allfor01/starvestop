package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionFunction {

    private final SubscriptionRepository subscriptionRepository;


    @Transactional
    public Subscription getById(Long id) {
        return subscriptionRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional
    public void decreaseById(Long id, Integer count) {
        Subscription subscription = getById(id);
        subscription.decrease(count);
    }

    @Transactional
    public void increaseById(Long id, Integer count) {
        Subscription subscription = getById(id);
        subscription.increase(count);
    }
}
