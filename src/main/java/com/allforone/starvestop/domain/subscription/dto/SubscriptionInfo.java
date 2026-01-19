package com.allforone.starvestop.domain.subscription.dto;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SubscriptionInfo {
    private final String name;
    private final BigDecimal price;
    private final LocalDateTime expiredAt;

    public static SubscriptionInfo from(UserSubscription userSubscription, Subscription subscription) {
        return new SubscriptionInfo(
                subscription.getSubscriptionName(),
                subscription.getPrice(),
                userSubscription.getExpiresAt()
        );
    }
}