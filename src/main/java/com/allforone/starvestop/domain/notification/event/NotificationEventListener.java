package com.allforone.starvestop.domain.notification.event;

import com.allforone.starvestop.domain.notification.service.NotificationService;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.event.PaymentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    public final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentStatusSuccess(PaymentStatusChangedEvent event) {

        if (event.status() != PaymentStatus.SUCCEEDED) {
            return;
        }

        notificationService.sendPaymentOwnerNotification(event.orderKey());
        notificationService.sendPaymentUserNotification(event.userId());
    }
}
