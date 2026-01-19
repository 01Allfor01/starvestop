package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.usersubscription.enums.Day;
import com.allforone.starvestop.domain.usersubscription.enums.MealTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetSubscriptionResponse {

    private final Long subscriptionId;
    private final Long storeId;
    private final String storeName;
    private final String subscriptionName;
    private final String subscriptionDescription;
    private final List<Day> dayList;
    private final List<MealTime> mealTimeList;
    private final BigDecimal price;
    private final Long stock;
    private final boolean isJoinable;

    public static GetSubscriptionResponse from(Subscription subscription) {
        List<Day> dayList = Day.from(subscription.getDay());
        List<MealTime> mealTimeList = MealTime.from(subscription.getMealTime());
        return new GetSubscriptionResponse(
                subscription.getId(),
                subscription.getStore().getId(),
                subscription.getStore().getStoreName(),
                subscription.getSubscriptionName(),
                subscription.getDescription(),
                dayList,
                mealTimeList,
                subscription.getPrice(),
                subscription.getStock(),
                subscription.isJoinable()
        );
    }
}
