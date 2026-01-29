package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.UserSubscription;
import com.allforone.starvestop.domain.subscription.enums.Day;
import com.allforone.starvestop.domain.subscription.enums.MealTime;
import com.allforone.starvestop.domain.subscription.enums.UserSubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserSubscriptionDetailResponse {
    private final Long id;
    private final Long userId;
    private final Long subscriptionId;
    private final String subscriptionName;
    private final Long storeId;
    private final String storeName;
    private final UserSubscriptionStatus status;
    private final List<Day> dayList;
    private final List<MealTime> mealTimeList;
    private final BigDecimal price;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public static GetUserSubscriptionDetailResponse from(UserSubscription userSubscription) {
        List<Day> dayList = Day.from(userSubscription.getSubscription().getDay());
        List<MealTime> mealTimeList = MealTime.from(userSubscription.getSubscription().getMealTime());
        return new GetUserSubscriptionDetailResponse(
                userSubscription.getId(),
                userSubscription.getUser().getId(),
                userSubscription.getSubscription().getId(),
                userSubscription.getSubscription().getName(),
                userSubscription.getSubscription().getStore().getId(),
                userSubscription.getSubscription().getStore().getName(),
                userSubscription.getStatus(),
                dayList,
                mealTimeList,
                userSubscription.getSubscription().getPrice(),
                userSubscription.getCreatedAt(),
                userSubscription.getExpiresAt()
        );
    }
}
