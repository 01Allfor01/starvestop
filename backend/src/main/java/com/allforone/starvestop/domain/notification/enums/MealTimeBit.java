package com.allforone.starvestop.domain.notification.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "식사시간 비트(MORNING=1, LUNCH=2, DINNER=4)")
@Getter
@RequiredArgsConstructor
public enum MealTimeBit {
    MORNING(1),     // 1<<0
    LUNCH(2),       // 1<<1
    DINNER(4);      // 1<<2

    private final int bit;
}
