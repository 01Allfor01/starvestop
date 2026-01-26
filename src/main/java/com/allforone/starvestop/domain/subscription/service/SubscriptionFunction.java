package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionFunction {

    private SubscriptionRepository subscriptionRepository;

    public Subscription getById(Long id) {
        return subscriptionRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }
}
