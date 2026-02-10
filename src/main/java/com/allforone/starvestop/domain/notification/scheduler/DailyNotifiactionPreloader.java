package com.allforone.starvestop.domain.notification.scheduler;

import com.allforone.starvestop.domain.notification.service.NotificationJobPreloaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class DailyNotifiactionPreloader {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final NotificationJobPreloaderService preloaderService;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void preloadToday() {
        preloaderService.preload(LocalDate.now(KST));
    }

}
