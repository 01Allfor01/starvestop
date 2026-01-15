package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UpdateSubscriptionResponse {

    private final Long subscriptionId;
    private final Long storeId;
    private final String subscriptionName;
    private final String subscriptionDescription;
    private final BigDecimal price;

    public static UpdateSubscriptionResponse from(Subscription subscription) {
        return new UpdateSubscriptionResponse(
                subscription.getId(),
                subscription.getStore().getId(),
                subscription.getSubscriptionName(),
                subscription.getDescription(),
                subscription.getPrice()
        );
    }
}
