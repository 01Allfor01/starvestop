package com.allforone.starvestop.domain.notification.scheduler;

import com.allforone.starvestop.domain.notification.service.UserNotificationService;
import com.allforone.starvestop.domain.notification.enums.DayBit;
import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionTimeNotificationScheduler {

    private final UserNotificationService userNotificationService;

    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Seoul")
    public void morning() { run(MealTimeBit.MORNING); }

    @Scheduled(cron = "0 0 12 * * *", zone = "Asia/Seoul")
    public void lunch() { run(MealTimeBit.LUNCH); }

    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void dinner() { run(MealTimeBit.DINNER); }

    private void run(MealTimeBit mealTime) {
        Integer dayBit = DayBit.todayBit();
        int mealTimeBit = mealTime.getBit();
        userNotificationService.sendSubscriptionTimeNotification(dayBit, mealTimeBit);
    }
}
