package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.dto.SubscriptionDto;
import com.allforone.starvestop.domain.subscription.enums.Day;
import com.allforone.starvestop.domain.subscription.enums.MealTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class GetSubscriptionDistanceResponse {
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
    private final Double distance;

    public static GetSubscriptionDistanceResponse from(SubscriptionDto subscriptionDto, Double distance) {
        List<Day> dayList = Day.from(subscriptionDto.day());
        List<MealTime> mealTimeList = MealTime.from(subscriptionDto.mealTime());
        return new GetSubscriptionDistanceResponse(
                subscriptionDto.id(),
                subscriptionDto.storeId(),
                subscriptionDto.storeName(),
                subscriptionDto.name(),
                subscriptionDto.description(),
                dayList,
                mealTimeList,
                subscriptionDto.price(),
                subscriptionDto.stock(),
                subscriptionDto.isJoinable(),
                distance
        );
    }
}
