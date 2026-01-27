package com.allforone.starvestop.domain.subscription.dto.response;

import com.allforone.starvestop.domain.subscription.entity.Subscription;
import com.allforone.starvestop.domain.subscription.enums.Day;
import com.allforone.starvestop.domain.subscription.enums.MealTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateSubscriptionResponse {
    private final Long id;
    private final Long storeId;
    private final String name;
    private final String description;
    private final List<Day> dayList;
    private final List<MealTime> mealTimeList;
    private final BigDecimal price;
    private final Integer stock;
    private final LocalDateTime createdAt;

    public static CreateSubscriptionResponse from(Subscription subscription) {
        List<Day> dayList = Day.from(subscription.getDay());
        List<MealTime> mealTimeList = MealTime.from(subscription.getMealTime());
        return new CreateSubscriptionResponse(
                subscription.getId(),
                subscription.getStore().getId(),
                subscription.getName(),
                subscription.getDescription(),
                dayList,
                mealTimeList,
                subscription.getPrice(),
                subscription.getStock(),
                subscription.getCreatedAt()
        );
    }
}
