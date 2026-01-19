package com.allforone.starvestop.domain.usersubscription.dto;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserSubscriptionInfo {
    private final String name;
    private final BigDecimal price;
    private final LocalDateTime expiredAt;

    public static UserSubscriptionInfo from(UserSubscription userSubscription, Subscription subscription) {
        return new UserSubscriptionInfo(
                subscription.getSubscriptionName(),
                subscription.getPrice(),
                userSubscription.getExpiresAt()
        );
    }
}