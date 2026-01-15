package com.allforone.starvestop.domain.usersubscription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserSubscriptionRequest {

    @NotNull(message = "요일을 선택 해 주세요")
    private int day;

    @NotNull(message = "식사 시간을 선택 해 주세요")
    private int mealTime;
}
