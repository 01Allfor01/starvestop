package com.allforone.starvestop.domain.usersubscription.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import com.allforone.starvestop.domain.usersubscription.dto.response.CreateUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.dto.response.GetUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import com.allforone.starvestop.domain.usersubscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    //사용자 구독 추가
    @Transactional
    public CreateUserSubscriptionResponse createUserSubscription(AuthUser authUser, Long subscriptionId) {
        User user = getUserOrThrow(authUser.getUserId());

        Subscription subscription = subscriptionRepository.findByIdAndIsDeletedIsFalse(subscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        boolean exist = userSubscriptionRepository.existsByUserAndSubscription(user, subscription);

        if (exist) {
            throw new CustomException(ErrorCode.USER_SUBSCRIPTION_EXIST);
        }

        UserSubscription userSubscription = UserSubscription.create(user, subscription);

        UserSubscription savedUserSubscription = userSubscriptionRepository.save(userSubscription);

        return CreateUserSubscriptionResponse.from(savedUserSubscription);
    }

    //사용자 구독 목록 조회
    @Transactional(readOnly = true)
    public List<GetUserSubscriptionResponse> getUserSubscriptions(AuthUser authUser) {
        User user = getUserOrThrow(authUser.getUserId());

        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAllByUserAndIsDeletedIsFalse(user);

        return userSubscriptionList.stream().map(GetUserSubscriptionResponse::from).toList();
    }

    //사용자 구독 상세 조회
    @Transactional(readOnly = true)
    public GetUserSubscriptionResponse getUserSubscription(AuthUser authUser, Long userSubscriptionId) {
        UserSubscription userSubscription = getUserSubscriptionOrThrow(userSubscriptionId);

        if (!userSubscription.getUser().getId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        return GetUserSubscriptionResponse.from(userSubscription);
    }

    //사용자 구독 취소
    @Transactional
    public void deleteUserSubscription(AuthUser authUser, Long userSubscriptionId) {
        UserSubscription userSubscription = getUserSubscriptionOrThrow(userSubscriptionId);

        if (!userSubscription.getUser().getId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        userSubscription.delete();
    }

    //사용자 확인
    private User getUserOrThrow(Long userId) {
        return userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    //사용자 구독 조회
    private UserSubscription getUserSubscriptionOrThrow(Long userSubscriptionId) {
        return userSubscriptionRepository.findByIdAndIsDeletedIsFalse(userSubscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));
    }
}
