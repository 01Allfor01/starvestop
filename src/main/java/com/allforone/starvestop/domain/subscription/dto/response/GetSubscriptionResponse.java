package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.enums.Day;
import com.allforone.starvestop.domain.subscription.enums.MealTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetSubscriptionResponse {
    private final Long id;
    private final Long storeId;
    private final String storeName;
    private final String name;
    private final String description;
    private final List<Day> dayList;
    private final List<MealTime> mealTimeList;
    private final BigDecimal price;
    private final Integer stock;
    private final boolean isJoinable;

    public static GetSubscriptionResponse from(Subscription subscription) {
        List<Day> dayList = Day.from(subscription.getDay());
        List<MealTime> mealTimeList = MealTime.from(subscription.getMealTime());
        return new GetSubscriptionResponse(
                subscription.getId(),
                subscription.getStore().getId(),
                subscription.getStore().getStoreName(),
                subscription.getName(),
                subscription.getDescription(),
                dayList,
                mealTimeList,
                subscription.getPrice(),
                subscription.getStock(),
                subscription.isJoinable()
        );
    }
}
