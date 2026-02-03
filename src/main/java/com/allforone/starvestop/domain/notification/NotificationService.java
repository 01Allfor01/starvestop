package com.allforone.starvestop.domain.notification;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class NotificationService {

    @Transactional
    public BatchResponse sendMultiNotification(NotificationDto notification, Set<String> tokens) {
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

//    @Transactional
//    public BatchResponse sendNotification(NotificationDto notification) {
//        try {
//            Message message = Message.builder()
//                .setToken(notification.getToken())
//                .setNotification(Notification.builder()
//                        .setTitle(notification.getTitle())
//                        .setBody(notification.getBody())
//                        .build())
//                .build();
//
//            return FirebaseMessaging.getInstance().send(message);
//        } catch (Exception e) {
//            throw new CustomException(ErrorCode.NOTIFICATION_SEND_FAIL);
//        }
//    }
}
