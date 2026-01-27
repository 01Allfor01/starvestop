package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.UserSubscription;
import com.allforone.starvestop.domain.subscription.enums.UserSubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetUserSubscriptionResponse {
    private final Long id;
    private final Long subscriptionId;
    private final String subscriptionName;
    private final Long storeId;
    private final String storeName;
    private final BigDecimal price;
    private final UserSubscriptionStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public static GetUserSubscriptionResponse from(UserSubscription userSubscription) {
        return new GetUserSubscriptionResponse(
                userSubscription.getId(),
                userSubscription.getSubscription().getId(),
                userSubscription.getSubscription().getName(),
                userSubscription.getSubscription().getStore().getId(),
                userSubscription.getSubscription().getStore().getStoreName(),
                userSubscription.getSubscription().getPrice(),
                userSubscription.getStatus(),
                userSubscription.getCreatedAt(),
                userSubscription.getExpiresAt()
        );
    }
}
