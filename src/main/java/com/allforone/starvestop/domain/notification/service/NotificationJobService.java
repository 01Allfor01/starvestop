package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.NotificationJob;
import com.allforone.starvestop.domain.notification.enums.DayBit;
import com.allforone.starvestop.domain.notification.enums.MealTimeBit;
import com.allforone.starvestop.domain.notification.repository.NotificationJobJdbcRepository;
import com.allforone.starvestop.domain.notification.repository.NotificationJobRepository;
import com.allforone.starvestop.domain.notification.repository.UserNotificationRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotificationJobService {

    private final ClearNotificationTokenService clearTokenService;
    private final UserNotificationRepository userNotificationRepository;
    private final NotificationJobJdbcRepository notificationJobJdbcRepository;
    private final NotificationJobRepository notificationJobRepository;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public void preload() {
        int dayBit = DayBit.todayBit();
        LocalDate sendDate = LocalDate.now(KST);

        final int pageSize = 5000;
        long cursor = 0L;

        while (true) {

            List<SendMealTimeNotificationDto> targets =
                    userNotificationRepository.findByTargetList(dayBit, cursor, pageSize);

            if (targets.isEmpty()) return;

            List<NotificationJob> jobs = targets.stream()
                    .flatMap(t -> Stream.of(MealTimeBit.MORNING, MealTimeBit.LUNCH, MealTimeBit.DINNER)
                            .filter(slot -> (t.mealTimeBit() & slot.getBit()) != 0)
                            .map(slot -> new NotificationJob(
                                    t.userId(),
                                    t.token() == null ? null : t.token().trim(),
                                    t.subscriptionName(),
                                    slot,
                                    sendDate
                            ))
                    )
                    .filter(j -> j.getToken() != null && !j.getToken().isBlank())
                    .toList();

            if (jobs.isEmpty()) return;

            try {
                notificationJobJdbcRepository.bulkInsertIgnore(jobs);
            } catch (DataIntegrityViolationException e) {
                //unique 충돌 무시
            }

            long nextCursor = targets.get(targets.size() - 1).cursorId();

            if (nextCursor <= cursor) break;

            cursor = nextCursor;
        }
    }

    @Transactional
    public void sendNotificationJob(LocalDate today, MealTimeBit mealTime) {

        while (true) {
            List<NotificationJob> jobList = notificationJobRepository.findTop500BySendDateAndMealTimeOrderByIdAsc(today, mealTime);

            if (jobList.isEmpty()) {
                break;
            }

            List<Message> messageList = getMessageList(jobList);

            try {
                BatchResponse response = FirebaseMessaging.getInstance().sendEach(messageList);
                int i = 0;
                List<String> invalidTokenList = new ArrayList<>();
                List<Long> deleteJobIdList = new ArrayList<>();

                for (SendResponse r : response.getResponses()) {
                    if (!r.isSuccessful()) {
                        FirebaseMessagingException e = r.getException();
                        MessagingErrorCode code = e.getMessagingErrorCode();

                        if (code.equals(MessagingErrorCode.UNREGISTERED)
                                || code.equals(MessagingErrorCode.INVALID_ARGUMENT)) {
                            invalidTokenList.add(jobList.get(i).getToken());
                        }
                        clearTokenService.invalidToken(invalidTokenList);
                    }

                    deleteJobIdList.add(jobList.get(i).getId());

                    ++i;


                }
                if (!invalidTokenList.isEmpty()) {
                    userNotificationRepository.deleteByTokenIn(invalidTokenList);
                }
                if (!deleteJobIdList.isEmpty()) {
                    notificationJobRepository.deleteAllByIdInBatch(deleteJobIdList);
                }

            } catch (FirebaseMessagingException e) {
                throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
            }
        }
    }

    private List<Message> getMessageList(List<NotificationJob> jobList) {
        List<Message> messageList = jobList.stream()
                .map(job -> Message.builder()
                        .setToken(job.getToken())
                        .setNotification(Notification.builder()
                                .setTitle("Starve stop")
                                .setBody(job.getSubscriptionName() + " 상품 수령 시간입니다")
                                .build())
                        .build()
                ).toList();
        return messageList;
    }
}
