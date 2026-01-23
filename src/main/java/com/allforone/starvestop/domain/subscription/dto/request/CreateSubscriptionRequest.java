package com.allforone.starvestop.domain.subscription.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateSubscriptionRequest {
    @NotBlank(message = "구독 이름을 적어주세요")
    @Size(max = 100, message = "구독 이름은 최대 100자까지 입력 가능합니다.")
    private String subscriptionName;

    @NotBlank(message = "구독 설명을 적어주세요")
    @Size(max = 255, message = "구독 설명은 최대 255자까지 입력 가능합니다.")
    private String description;

    @NotNull(message = "요일을 선택 해 주세요")
    @Min(value = 1, message = "요일을 잘못 입력했습니다")
    @Max(value = 127, message = "요일을 잘못 입력했습니다")
    private int day;

    @NotNull(message = "식사 시간을 선택 해 주세요")
    @Min(value = 1, message = "식사 시간을 잘못 입력했습니다")
    @Max(value = 7, message = "식사 시간을 잘못 입력했습니다")
    private int mealTime;

    @NotNull(message = "가격을 적어주세요")
    private BigDecimal price;

    @NotNull(message = "재고를 적어주세요")
    @Max(value = 20000, message = "재고 입력값이 최대값을 초과했습니다")
    private Long stock;
}
