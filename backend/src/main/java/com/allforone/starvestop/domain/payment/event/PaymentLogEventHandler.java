package com.allforone.starvestop.domain.payment.event;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.service.PaymentLogService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentLogEventHandler {

    private final PaymentLogService paymentLogService;

    public PaymentLogEventHandler(PaymentLogService paymentLogService) {
        this.paymentLogService = paymentLogService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentStatusChangedEvent e) {
        if (!(e.status() == PaymentStatus.REQUESTED ||
                e.status() == PaymentStatus.SUCCEEDED ||
                e.status() == PaymentStatus.FAILED_NON_RETRYABLE ||
                e.status() == PaymentStatus.FAILED_RETRYABLE || e.status() == PaymentStatus.PENDING
        )) {
            return;
        }

        paymentLogService.save(
                e.paymentId(),
                e.userId(),
                e.orderKey(),
                e.status(),
                e.payload()
        );
    }
}