package com.allforone.starvestop.domain.notification.event;

import com.allforone.starvestop.domain.notification.service.UserNotificationService;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.event.PaymentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final UserNotificationService userNotificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentStatusSuccess(PaymentStatusChangedEvent event) {

        if (event.status() == PaymentStatus.FAILED_NON_RETRYABLE
                || event.status() == PaymentStatus.FAILED_RETRYABLE) {
            userNotificationService.sendPaymentUserNotification(event.userId(), event.status());
            return;
        }

        if (event.status() == PaymentStatus.SUCCEEDED) {
            userNotificationService.sendPaymentOwnerNotification(event.orderKey());
            userNotificationService.sendPaymentUserNotification(event.userId(), event.status());
        }
    }
}
