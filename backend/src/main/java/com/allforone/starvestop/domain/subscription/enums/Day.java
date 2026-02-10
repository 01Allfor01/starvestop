package com.allforone.starvestop.domain.subscription.enums;

import java.util.ArrayList;
import java.util.List;

public enum Day {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(4),
    THURSDAY(8),
    FRIDAY(16),
    SATURDAY(32),
    SUNDAY(64);

    private final int value;

    Day(int value) {
        this.value = value;
    }

    public boolean isSelected(int input) {
        return (input & value) == value;
    }

    public static List<Day> from(int input) {
        List<Day> selectedDays = new ArrayList<>();
        for (Day day : values()) {
            if (day.isSelected(input)) {
                selectedDays.add(day);
            }
        }
        return selectedDays;
    }
}
