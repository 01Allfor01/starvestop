package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetSubscriptionResponse {

    private final Long Id;
    private final String subscriptionName;
    private final String subscriptionDescription;
    private final BigDecimal price;

    public static GetSubscriptionResponse from(Subscription subscription) {
        return new GetSubscriptionResponse(
                subscription.getId(),
                subscription.getSubscriptionName(),
                subscription.getDescription(),
                subscription.getPrice()
        );
    }
}
