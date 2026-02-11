package com.allforone.starvestop.domain.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MealTimeBit {
    MORNING(1),     // 1<<0
    LUNCH(2),       // 1<<1
    DINNER(4);      // 1<<2

    private final int bit;
}
