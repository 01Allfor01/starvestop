package com.allforone.starvestop.domain.notification;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.notification.dto.NotificationDto;
import com.allforone.starvestop.domain.notification.dto.NotificationMulticastRequest;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class NotificationService {

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
        } catch (Exception e) {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }

    @Transactional
    public void sendPaymentNotification(String token, String orderName, String amount) {
        String title = orderName + "에서" + amount + "원이 결제되었습니다";
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody("")
                            .build())
                    .build();
        } catch(Exception e) {
            throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }


}
