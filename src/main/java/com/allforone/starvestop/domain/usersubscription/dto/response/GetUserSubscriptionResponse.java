package com.allforone.starvestop.domain.usersubscription.dto.response;

import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import com.allforone.starvestop.domain.usersubscription.enums.Day;
import com.allforone.starvestop.domain.usersubscription.enums.MealTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetUserSubscriptionResponse {

    private final Long userSubscriptionId;
    private final String subscriptionName;
    private final String storeName;
    private final BigDecimal price;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private final List<Day> dayList;
    private final List<MealTime> mealTimeList;
    private final Boolean isExpired;

    public static GetUserSubscriptionResponse from(UserSubscription userSubscription) {

        List<Day> dayList = Day.from(userSubscription.getDay());
        List<MealTime> mealTimeList = MealTime.from(userSubscription.getMealTime());
        Boolean isExpired = LocalDateTime.now().isAfter(userSubscription.getExpiresAt());

        return new GetUserSubscriptionResponse(
                userSubscription.getId(),
                userSubscription.getSubscription().getSubscriptionName(),
                userSubscription.getSubscription().getStore().getStoreName(),
                userSubscription.getSubscription().getPrice(),
                userSubscription.getCreatedAt(),
                userSubscription.getExpiresAt(),
                dayList,
                mealTimeList,
                isExpired
        );
    }
}
