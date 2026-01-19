package com.allforone.starvestop.domain.usersubscription.dto.response;

import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import com.allforone.starvestop.domain.usersubscription.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetUserSubscriptionResponse {

    private final Long userSubscriptionId;
    private final String subscriptionName;
    private final String storeName;
    private final BigDecimal price;
    private final Status status;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public static GetUserSubscriptionResponse from(UserSubscription userSubscription) {
        return new GetUserSubscriptionResponse(
                userSubscription.getId(),
                userSubscription.getSubscription().getSubscriptionName(),
                userSubscription.getSubscription().getStore().getStoreName(),
                userSubscription.getSubscription().getPrice(),
                userSubscription.getStatus(),
                userSubscription.getCreatedAt(),
                userSubscription.getExpiresAt()
        );
    }
}
