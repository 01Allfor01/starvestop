package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.NotificationJob;
import com.allforone.starvestop.domain.notification.enums.DayBit;
import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import com.allforone.starvestop.domain.notification.repository.NotificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationJobPreloaderService {

    NotificationTokenRepository notificationTokenRepository;

    public void preload(LocalDate day) {
        int dayBit = DayBit.todayBit();

        preloadSlot(day, dayBit, MealTimeBit.MORNING, LocalTime.of(7, 0));
        preloadSlot(day, dayBit, MealTimeBit.LUNCH,   LocalTime.of(12, 0));
        preloadSlot(day, dayBit, MealTimeBit.DINNER,  LocalTime.of(18, 0));
    }

    private void preloadSlot(LocalDate date, int dayBit, MealTimeBit slot, LocalTime sendTime) {
        int mealBit = slot.getBit();

        List<SendMealTimeNotificationDto> targets =
                notificationTokenRepository.findByMealTime(dayBit, mealBit);

        if (targets.isEmpty()) return;

        LocalDateTime sendAt = LocalDateTime.of(date, sendTime);

        jobJdbcRepository.bulkInsertIgnore(rows);

        List<NotificationJob> jobs = targets.stream()
                .map(t -> new NotificationJob(
                        t.getUserId(),
                        t.getToken().trim(),
                        t.getSubscriptionId(),
                        today,
                        mealTime,
                        t.getRole()
                ))
                .toList();

    }
}
