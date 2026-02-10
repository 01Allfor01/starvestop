package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.domain.notification.enums.DayBit;
import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class NotificationJobPreloaderService {
    public void preload(LocalDate day) {
        int dayBit = DayBit.todayBit(); // 너희 매핑 (월1 화2 ...)

        preloadSlot(day, dayBit, MealTimeBit.MORNING, LocalTime.of(7, 0));
        preloadSlot(day, dayBit, MealTimeBit.LUNCH,   LocalTime.of(12, 0));
        preloadSlot(day, dayBit, MealTimeBit.DINNER,  LocalTime.of(18, 0));
    }
}
