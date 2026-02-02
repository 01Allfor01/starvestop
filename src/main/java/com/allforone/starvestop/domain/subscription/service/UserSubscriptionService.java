package com.allforone.starvestop.domain.subscription.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.BillingKeyCrypto;
import com.allforone.starvestop.domain.payment.entity.UserBilling;
import com.allforone.starvestop.domain.payment.enums.BillingStatus;
import com.allforone.starvestop.domain.payment.infra.TossBillingClient;
import com.allforone.starvestop.domain.subscription.dto.response.GetUserSubscriptionDetailResponse;
import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.enums.UserSubscriptionStatus;
import com.allforone.starvestop.domain.subscription.repository.SubscriptionRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.subscription.dto.response.CreateUserSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetUserSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.entity.UserSubscription;
import com.allforone.starvestop.domain.subscription.repository.UserSubscriptionRepository;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSubscriptionService {

    private final UserService userService;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final TossBillingClient tossBillingClient;
    private final BillingKeyCrypto billingKeyCrypto;

    //사용자 구독 추가
    @Transactional
    public CreateUserSubscriptionResponse createUserSubscription(AuthUser authUser, Long subscriptionId) {
        //1. 사용자 확인
        User user = userService.getById(authUser.getUserId());

        //2. 구독 확인
        Subscription subscription = subscriptionRepository.findByIdAndIsDeletedIsFalse(subscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        //3. 활성화 중인 구독이 있으면 예외 처리
        boolean exist = userSubscriptionRepository.existsByUserAndSubscriptionAndIsDeletedIsFalse(user, subscription);
        if (exist) {
            throw new CustomException(ErrorCode.USER_SUBSCRIPTION_EXIST);
        }

        //4. 해지된 구독도 조회
        Optional<UserSubscription> optional = userSubscriptionRepository.findByUserAndSubscription(user, subscription);

        UserSubscription userSubscription;

        //5. 사용자 구독이 있는데 soft delete된 상태 -> 다시 활성화
        if (optional.isPresent()) {
            userSubscription = optional.get();
            userSubscription.recovery();
        } else {
            //6. 사용자 구독이 없으면 신규 가입
            userSubscription = UserSubscription.create(user, subscription);
            userSubscriptionRepository.save(userSubscription);
        }

        return CreateUserSubscriptionResponse.from(userSubscription);
    }

    //사용자 구독 목록 조회
    @Transactional(readOnly = true)
    public List<GetUserSubscriptionResponse> getUserSubscriptions(AuthUser authUser) {
        User user = userService.getById(authUser.getUserId());

        List<UserSubscription> userSubscriptionList = userSubscriptionRepository.findAllByUserAndIsDeletedIsFalse(user);

        return userSubscriptionList.stream().map(GetUserSubscriptionResponse::from).toList();
    }

    //사용자 구독 상세 조회
    @Transactional(readOnly = true)
    public GetUserSubscriptionDetailResponse getUserSubscription(AuthUser authUser, Long userSubscriptionId) {
        UserSubscription userSubscription = getUserSubscriptionOrThrow(userSubscriptionId);

        checkPermission(authUser, userSubscription);

        return GetUserSubscriptionDetailResponse.from(userSubscription);
    }

    //사용자 구독 취소
    @Transactional
    public void deleteUserSubscription(AuthUser authUser, Long userSubscriptionId) {
        UserSubscription userSubscription = getUserSubscriptionOrThrow(userSubscriptionId);

        checkPermission(authUser, userSubscription);

        userSubscription.delete();

    }

    //본인 구독 확인
    @Transactional
    public void checkPermission(AuthUser authUser, UserSubscription userSubscription) {
        if (!userSubscription.getUser().getId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    //사용자 구독 조회
    @Transactional
    public UserSubscription getUserSubscriptionOrThrow(Long userSubscriptionId) {
        return userSubscriptionRepository.findByIdAndIsDeletedIsFalse(userSubscriptionId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional
    public void activate(Long userId, Long subscriptionId, UserBilling billing) {
        UserSubscription us = userSubscriptionRepository
                .findByUserIdAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        us.activate(billing, LocalDateTime.now());
    }

    @Transactional
    public int chargeDueSubscriptions() {
        LocalDateTime now = LocalDateTime.now();

        List<UserSubscription> dueList =
                userSubscriptionRepository.findAllByStatusAndExpiresAtLessThanEqual(UserSubscriptionStatus.ACTIVE, now);

        int successCount = 0;

        for (UserSubscription us : dueList) {
            try {
                UserBilling billing = us.getBilling();
                if (billing == null || billing.getStatus() != BillingStatus.ACTIVE) {
                    us.onChargeFailed();
                    continue;
                }

                String billingKeyPlain = billingKeyCrypto.decrypt(billing.getEncryptedBillingKey());
                String customerKey = billing.getCustomerKey();

                String orderId = "sub_" + us.getId() + "_" + UUID.randomUUID();
                String orderName = "subscription";
                long amount = resolveAmount(us);

                tossBillingClient.approveBilling(billingKeyPlain, customerKey, orderId, amount, orderName);

                billing.markUsed(now);
                us.onChargeSuccess(now);
                successCount++;

            } catch (Exception e) {
                us.onChargeFailed();
            }
        }

        return successCount;
    }

    private long resolveAmount(UserSubscription us) {
        return 9900L;
    }
}
