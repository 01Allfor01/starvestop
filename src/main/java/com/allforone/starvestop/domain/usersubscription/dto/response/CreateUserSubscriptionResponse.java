package com.allforone.starvestop.domain.usersubscription.dto.response;

import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateUserSubscriptionResponse {

    private final Long userSubscriptionId;
    private final Long userId;
    private final Long subscriptionId;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public static CreateUserSubscriptionResponse from(UserSubscription userSubscription) {
        return new CreateUserSubscriptionResponse(
                userSubscription.getId(),
                userSubscription.getUser().getId(),
                userSubscription.getSubscription().getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1)
        );
    }
}
