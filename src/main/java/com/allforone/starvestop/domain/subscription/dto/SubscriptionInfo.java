package com.allforone.starvestop.domain.subscription.dto;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
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
public class SubscriptionInfo {
    private final String name;
    private final BigDecimal price;
    private final LocalDateTime expiredAt;
    private final boolean isExpired;
    private final List<MealTime> mealTimes;
    private final List<Day> days;

    public static SubscriptionInfo from(UserSubscription userSubscription, Subscription subscription) {
        return new SubscriptionInfo(
                subscription.getSubscriptionName(),
                subscription.getPrice(),
                userSubscription.getExpiresAt(),
                userSubscription.isExpired(),
                MealTime.from(userSubscription.getMealTime()),
                Day.from(userSubscription.getDay())
        );
    }
}