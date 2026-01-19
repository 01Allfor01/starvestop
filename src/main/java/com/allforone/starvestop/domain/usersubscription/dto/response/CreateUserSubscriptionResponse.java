package com.allforone.starvestop.domain.usersubscription.dto.response;

import com.allforone.starvestop.domain.usersubscription.entity.UserSubscription;
import com.allforone.starvestop.domain.usersubscription.enums.Day;
import com.allforone.starvestop.domain.usersubscription.enums.MealTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateUserSubscriptionResponse {

    private final Long userSubscriptionId;
    private final Long userId;
    private final Long subscriptionId;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    private final List<Day> dayList;
    private final List<MealTime> mealTimeList;

    public static CreateUserSubscriptionResponse from(UserSubscription userSubscription) {
        List<Day> dayList = Day.from(userSubscription.getDay());
        List<MealTime> mealTimeList = MealTime.from(userSubscription.getMealTime());
        return new CreateUserSubscriptionResponse(
                userSubscription.getId(),
                userSubscription.getUser().getId(),
                userSubscription.getSubscription().getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1),
                dayList,
                mealTimeList
        );
    }
}
