package com.allforone.starvestop.domain.payment.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.common.utils.BillingKeyCrypto;
import com.allforone.starvestop.domain.payment.entity.UserBilling;
import com.allforone.starvestop.domain.payment.repository.BillingRepository;
import com.allforone.starvestop.domain.payment.infra.TossBillingClient;
import com.allforone.starvestop.domain.subscription.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final TossBillingClient tossBillingClient;
    private final BillingKeyCrypto billingKeyCrypto;
    private final UserSubscriptionService userSubscriptionService;

    @Transactional
    public UserBilling issueAndSave(Long userId, String customerKey, String authKey) {

        // 유저당 빌링 1개
        billingRepository.findByUserId(userId).ifPresent(b -> {
            throw new CustomException(ErrorCode.SUBSCRIPTION_BILLING_ALREADY_EXISTS);
        });

        Map<String, Object> response;
        try {
            response = tossBillingClient.issueBillingKey(authKey, customerKey);
        } catch (WebClientResponseException e) {
            // 토스가 4xx/5xx 주는 케이스
            throw new CustomException(ErrorCode.BILLING_KEY_ISSUE_FAILED);
        } catch (Exception e) {
            // 타임아웃/네트워크 등
            throw new CustomException(ErrorCode.BILLING_KEY_ISSUE_FAILED);
        }

        if (response == null || response.get("billingKey") == null) {
            throw new CustomException(ErrorCode.BILLING_KEY_ISSUE_FAILED);
        }

        String billingKeyPlain = response.get("billingKey").toString();
        String encrypted = billingKeyCrypto.encrypt(billingKeyPlain);

        LocalDateTime now = LocalDateTime.now();
        UserBilling billing = UserBilling.create(userId, customerKey, encrypted, now);

        try {
            return billingRepository.save(billing);
        } catch (DataIntegrityViolationException e) {
            // 동시 요청 레이스로 unique(userId) 위반 시 500 방지
            throw new CustomException(ErrorCode.SUBSCRIPTION_BILLING_ALREADY_EXISTS);
        }
    }

    @Transactional
    public void confirmAndActivate(Long userId, String customerKey, String authKey, Long subscriptionId) {
        UserBilling billing = issueAndSave(userId, customerKey, authKey);
        userSubscriptionService.activate(userId, subscriptionId, billing);
    }
}
