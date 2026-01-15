package com.allforone.starvestop.domain.usersubscription.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import com.allforone.starvestop.domain.usersubscription.dto.request.CreateUserSubscriptionRequest;
import com.allforone.starvestop.domain.usersubscription.dto.response.CreateUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.dto.response.GetUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import com.allforone.starvestop.domain.usersubscription.repository.UserSubscriptionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Transactional
    public CreateUserSubscriptionResponse createUserSubscription(
            AuthUser authUser, Long subscriptionId,
            @Valid CreateUserSubscriptionRequest request
    ) {

        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND)
        );

        UserSubscription userSubscription = UserSubscription.create(user, subscription, request.getDay(), request.getMealTime());
        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        return CreateUserSubscriptionResponse.from(savedUserSubscription);
    }

    @Transactional(readOnly = true)
    public List<GetUserSubscriptionResponse> getUserSubscriptions(AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAllByUser(user);
        log.info("userSubscriptionList={}", userSubscriptionList);
        return userSubscriptionList.stream().map(GetUserSubscriptionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetUserSubscriptionResponse getUserSubscription(Long userSubscriptionId) {

        UserSubscription userSubscription = userSubscriptionRepository.findById(userSubscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND)
        );

        return GetUserSubscriptionResponse.from(userSubscription);
    }
}
