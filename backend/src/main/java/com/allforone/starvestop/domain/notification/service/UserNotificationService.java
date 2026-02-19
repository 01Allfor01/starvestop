package com.allforone.starvestop.domain.notification.service;

import com.allforone.starvestop.common.enums.UserRole;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.notification.dto.NotificationDto;
import com.allforone.starvestop.domain.notification.dto.NotificationMulticastRequest;
import com.allforone.starvestop.domain.notification.entity.UserNotification;
import com.allforone.starvestop.domain.notification.enums.NotificationPlatformType;
import com.allforone.starvestop.domain.notification.repository.UserNotificationRepository;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.service.OrderProductService;
import com.allforone.starvestop.domain.owner.service.OwnerService;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import com.allforone.starvestop.domain.user.service.UserService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final UserService userService;
    private final OwnerService ownerService;
    private final PaymentService paymentService;
    private final ClearNotificationTokenService clearTokenService;
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

        UserNotification userNotification = UserNotification.createToken(userId, token, platform, role);
        userNotificationRepository.save(userNotification);

    }

    //
    @Transactional
    public BatchResponse sendMultiNotification(Long subscriptionId, NotificationMulticastRequest notification) {

        List<String> tokenList = userNotificationRepository.findUserTokenBySubscriptionId(subscriptionId);

        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokenList)
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


    //결제 메세지 전송

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPaymentUserNotification(Long userId, PaymentStatus status) {

        UserNotification userNotification = userNotificationRepository.findByUserId(userId);

        if (userNotification == null) {
            return;
        }

        Message message = Message.builder()
                .setToken(userNotification.getToken())
                .setNotification(Notification.builder()
                        .setTitle("Starve stop")
                        .setBody(status.equals(PaymentStatus.SUCCEEDED)
                                ? "결제가 완료되었습니다"
                                : "결제가 실패되었습니다")
                        .build())
                .build();
        try {

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            invalidToken(userNotification.getToken(), e);
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

        UserNotification userNotification = userNotificationRepository.findOwnerTokenByOrderId(orderId);

        if (userNotification == null) {
            return;
        }

        Message message = Message.builder()
                .setToken(userNotification.getToken())
                .setNotification(Notification.builder()
                        .setTitle("주문 들어왔습니다")
                        .setBody(body)
                        .build())
                .build();
        try {

            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            invalidToken(userNotification.getToken(), e);
        }
    }

    private void invalidToken(String token, FirebaseMessagingException e) {
        MessagingErrorCode code = e.getMessagingErrorCode();

        if (code.equals(MessagingErrorCode.UNREGISTERED)
                || code.equals(MessagingErrorCode.INVALID_ARGUMENT)) {
            clearTokenService.invalidToken(token);
        }
    }
}
