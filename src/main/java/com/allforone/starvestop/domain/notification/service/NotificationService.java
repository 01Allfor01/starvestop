package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.notification.dto.NotificationDto;
import com.allforone.starvestop.domain.notification.dto.NotificationMulticastRequest;
import com.allforone.starvestop.domain.notification.dto.SendMealTimeNotificationDto;
import com.allforone.starvestop.domain.notification.entity.NotificationToken;
import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
import com.allforone.starvestop.domain.notification.repository.NotificationTokenRepository;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.service.OrderProductService;
import com.allforone.starvestop.domain.owner.service.OwnerService;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import com.allforone.starvestop.domain.user.enums.UserRole;
import com.allforone.starvestop.domain.user.service.UserService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationTokenRepository notificationTokenRepository;
    private final UserService userService;
    private final OwnerService ownerService;
    private final PaymentService paymentService;
    private final ClearNotificationTokenService clearNotificationTokenService;
    private final OrderProductService orderProductService;


    //토큰 저장
    @Transactional
    public void saveToken(Long userId, UserRole role, String token, NotificationPlatformType platform) {

        if (role.equals(UserRole.USER)) {
            userService.getById(userId);
        }

        if (role.equals(UserRole.OWNER)) {
            ownerService.getById(userId);
        }

        NotificationToken notificationToken = NotificationToken.createToken(userId, token, platform, role);
        notificationTokenRepository.save(notificationToken);

    }

    //
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


    //
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPaymentUserNotification(Long userId) {

        NotificationToken notificationToken = notificationTokenRepository.findByUserId(userId);

        if (notificationToken == null) {
            return;
        }

        Message message = Message.builder()
                .setToken(notificationToken.getToken())
                .setNotification(Notification.builder()
                        .setTitle("Starve stop")
                        .setBody("결제가 완료되었습니다")
                        .build())
                .build();
        try {

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            invalidToken(notificationToken.getToken(), e);
        }
    }

    @Transactional
    public void sendPaymentOwnerNotification(String orderKey) {

        Payment payment = paymentService.getByOrderKey(orderKey);
        Long orderId = payment.getOrder().getId();
        List<OrderProduct> productList = orderProductService.findListByOrderId(orderId);

        String body = productList.stream()
                .map(p -> "%s(%d)".formatted(p.getProductName(), p.getQuantity()))
                .collect(Collectors.joining("\n"));

        NotificationToken notificationToken = notificationTokenRepository.findOwnerTokenByOrderId(orderId);

        if (notificationToken == null) {
            return;
        }

        Message message = Message.builder()
                .setToken(notificationToken.getToken())
                .setNotification(Notification.builder()
                        .setTitle("주문 들어왔습니다")
                        .setBody(body)
                        .build())
                .build();
        try {

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            invalidToken(notificationToken.getToken(), e);
        }
    }

    @Transactional
    public void sendSubscriptionTimeNotification(Integer dayBit, Integer mealTimeBit) {

        List<SendMealTimeNotificationDto> dtoList = notificationTokenRepository.findByMealTime(dayBit, mealTimeBit);

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

    private void invalidToken(String token, FirebaseMessagingException e) {
        MessagingErrorCode code = e.getMessagingErrorCode();

        if (code.equals(MessagingErrorCode.UNREGISTERED)
                || code.equals(MessagingErrorCode.INVALID_ARGUMENT)) {
            notificationTokenRepository.deleteByToken(token);
        }
    }
}
