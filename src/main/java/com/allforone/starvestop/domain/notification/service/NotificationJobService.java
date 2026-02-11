package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.NotificationJob;
import com.allforone.starvestop.domain.notification.enums.DayBit;
import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import com.allforone.starvestop.domain.notification.repository.NotificationJobJdbcRepository;
import com.allforone.starvestop.domain.notification.repository.NotificationTokenRepository;
import com.allforone.starvestop.domain.subscription.enums.Day;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationJobService {

    NotificationTokenRepository notificationTokenRepository;
    NotificationJobJdbcRepository notificationJobJdbcRepository;

    @Transactional
    public void preload() {

        preloadSlot(MealTimeBit.MORNING);
        preloadSlot(MealTimeBit.LUNCH);
        preloadSlot(MealTimeBit.DINNER);
    }

    @Transactional
    public void preloadSlot(MealTimeBit mealTime) {
        int dayBit = DayBit.todayBit();
        int mealBit = mealTime.getBit();

        List<SendMealTimeNotificationDto> targets =
                notificationTokenRepository.findByTargetList(dayBit);

        if (targets.isEmpty()) return;

        List<NotificationJob> jobs = targets.stream()
                .map(t -> new NotificationJob(
                        t.userId(),
                        t.token() == null ? null : t.token().trim(),
                        t.subscriptionName(),
                        mealTime
                ))
                .filter(j -> j.getToken() != null && !j.getToken().isBlank())
                .toList();

        if (jobs.isEmpty()) return;

        try {
            notificationJobJdbcRepository.bulkInsertIgnore(jobs);
        } catch (DataIntegrityViolationException e) {
            //unique 충돌 무시
        }
    }

    @Transactional
    public void sendNotificationJob(DayBit dayBit, MealTimeBit mealTimeBit) {
        List<SendMealTimeNotificationDto> dtoList = notificationTokenRepository.findByTargetList(dayBit);

        if (dtoList.isEmpty()) {
            return;
        }

        List<Message> messageList = dtoList.stream()
                .map(dto -> Message.builder()
                        .setToken(dto.token())
                        .setNotification(Notification.builder()
                                .setTitle("Starve stop")
                                .setBody(dto.subscriptionName() + " 상품 수령 시간입니다")
                                .build())
                        .build()
                ).toList();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEach(messageList);
            int i = 0;
            for (SendResponse r : response.getResponses()) {
                if (!r.isSuccessful()) {
                    FirebaseMessagingException e = r.getException();
                    invalidToken(dtoList.get(i).token(), e);
                }
                ++i;
            }

        } catch (FirebaseMessagingException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }
}
