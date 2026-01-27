package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateSubscriptionResponse {
    private final Long id;
    private final Long storeId;
    private final boolean isJoinable;

    public static UpdateSubscriptionResponse from(Subscription subscription) {
        return new UpdateSubscriptionResponse(
                subscription.getId(),
                subscription.getStore().getId(),
                subscription.isJoinable()
        );
    }
}
