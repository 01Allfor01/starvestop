package com.allforone.starvestop.domain.usersubscription.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserSubscriptionRequest {

    @NotNull(message = "요일을 선택 해 주세요")
    @Min(value = 1)
    @Max(value = 127)
    private int day;

    @NotNull(message = "식사 시간을 선택 해 주세요")
    @Min(value = 1)
    @Max(value = 7)
    private int mealTime;
}
