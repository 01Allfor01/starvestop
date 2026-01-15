package com.allforone.starvestop.domain.usersubscription.enums;

import java.util.ArrayList;
import java.util.List;

public enum MealTime {
    BREAKFAST(1),
    LUNCH(2),
    DINNER(4);


    private final int value;

    MealTime(int value) {
        this.value = value;
    }

    public boolean isSelected(int input) {
        return (input & value) == value;
    }

    public static List<MealTime> from(int input) {
        List<MealTime> selectedMealTimes = new ArrayList<>();
        for (MealTime mealTime : values()) {
            if (mealTime.isSelected(input)) {
                selectedMealTimes.add(mealTime);
            }
        }
        return selectedMealTimes;
    }
}
