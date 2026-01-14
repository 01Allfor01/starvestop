package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateSubscriptionResponse {

    private final Long subscriptionId;
    private final Long storeId;
    private final String subscriptionName;
    private final BigDecimal price;
    private final LocalDateTime createdAt;

    public static CreateSubscriptionResponse from(Subscription subscription) {
        return new CreateSubscriptionResponse(
                subscription.getId(),
                subscription.getStore().getId(),
                subscription.getSubscriptionName(),
                subscription.getPrice(),
                subscription.getCreatedAt()
        );
    }
}
