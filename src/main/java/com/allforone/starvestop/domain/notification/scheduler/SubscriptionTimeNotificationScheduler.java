package com.allforone.starvestop.domain.notification.scheduler;

import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import com.allforone.starvestop.domain.notification.service.NotificationJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class SubscriptionTimeNotificationScheduler {

    private final NotificationJobService notificationJobService;

    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Seoul")
    public void morning() {
        run(MealTimeBit.MORNING);
    }

    @Scheduled(cron = "0 0 12 * * *", zone = "Asia/Seoul")
    public void lunch() {
        run(MealTimeBit.LUNCH);
    }

    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void dinner() {
        run(MealTimeBit.DINNER);
    }

    private void run(MealTimeBit mealTime) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        notificationJobService.sendNotificationJob(today, mealTime);
    }
}
