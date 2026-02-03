package com.allforone.starvestop.domain.subscription.scheduler;

import com.allforone.starvestop.domain.subscription.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionBillingScheduler {

    private final UserSubscriptionService userSubscriptionService;

    @Scheduled(fixedDelay = 60 * 60 * 1000L)
    public void run() {
        userSubscriptionService.chargeDueSubscriptions();
    }
}
