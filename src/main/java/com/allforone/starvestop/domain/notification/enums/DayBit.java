package com.allforone.starvestop.domain.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public enum DayBit {
    MONDAY(1),      // 1<<0
    TUESDAY(2),     // 1<<1
    WEDNESDAY(4),   // 1<<2
    THURSDAY(8),    // 1<<3
    FRIDAY(16),     // 1<<4
    SATURDAY(32),   // 1<<5
    SUNDAY(64);     // 1<<6

    private final int bit;

    public static int todayBit() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        return switch (day) {
            case MONDAY -> MONDAY.bit;
            case TUESDAY -> TUESDAY.bit;
            case WEDNESDAY -> WEDNESDAY.bit;
            case THURSDAY -> THURSDAY.bit;
            case FRIDAY -> FRIDAY.bit;
            case SATURDAY -> SATURDAY.bit;
            case SUNDAY -> SUNDAY.bit;
        };
    }
}
