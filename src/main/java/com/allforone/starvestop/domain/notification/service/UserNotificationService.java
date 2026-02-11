package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.notification.dto.NotificationDto;
import com.allforone.starvestop.domain.notification.dto.NotificationMulticastRequest;
import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.UserNotification;
import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
import com.allforone.starvestop.domain.notification.repository.UserNotificationRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final UserService userService;


    //토큰 저장
    @Transactional
    public void saveToken(Long userId, String token, NotificationPlatformType platform) {
        User user = userService.getById(userId);
        UserNotification userNotification = UserNotification.create(user, token, platform);
        userNotificationRepository.save(userNotification);

    }

    @Transactional
    public BatchResponse sendMultiNotification(Long subscriptionId, NotificationMulticastRequest notification, Set<String> tokens) {
        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build())
                    .build();

            return FirebaseMessaging.getInstance().sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }

    @Transactional
    public String sendNotification(NotificationDto notification) {
        try {
            Message message = Message.builder()
                    .setToken(notification.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build())
                    .build();

            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            invalidToken(notification.getToken(), e);
        }
        return "알림 전송에 실패했습니다";
    }

    //결제 완료 메세지 전송
    @Transactional
    public void sendPaymentNotification(Long userId) {

        UserNotification userNotification = userNotificationRepository.findByUserId(userId);

        if (userNotification == null) {
            return;
        }

        try {
            Message message = Message.builder()
                    .setToken(userNotification.getToken())
                    .setNotification(Notification.builder()
                            .setTitle("Starve stop")
                            .setBody("결제가 완료되었습니다")
                            .build())
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            invalidToken(userNotification.getToken(), e);
        }
    }

    @Transactional(readOnly = true)
    public void sendSubscriptionTimeNotification(Integer dayBit, Integer mealTimeBit) {

        userNotificationRepository.findByMealTime(dayBit, mealTimeBit);

        List<SendMealTimeNotificationDto> dtoList = userNotificationRepository.findByMealTime(dayBit, mealTimeBit);

        if (dtoList.isEmpty()) {
            return;
        }

        try {
            List<Message> messageList = dtoList.stream()
                            .map(dto -> Message.builder()
                                    .setToken(dto.token())
                                    .setNotification(Notification.builder()
                                            .setTitle("Starve stop")
                                            .setBody(dto.subscriptionName() + " 상품 수령 시간입니다")
                                            .build())
                                    .build()
                            ).toList();

            FirebaseMessaging.getInstance().sendEach(messageList);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }

    private void invalidToken(String token, FirebaseMessagingException e) {
        MessagingErrorCode code = e.getMessagingErrorCode();

        if (code.equals(MessagingErrorCode.UNREGISTERED)
                || code.equals(MessagingErrorCode.INVALID_ARGUMENT)) {
            userNotificationRepository.deleteByToken(token);
        }
    }
}
