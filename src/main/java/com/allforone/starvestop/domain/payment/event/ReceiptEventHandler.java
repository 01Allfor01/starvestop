package com.allforone.starvestop.domain.payment.event;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import com.allforone.starvestop.domain.payment.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ReceiptEventHandler {

    private final PaymentService paymentService;
    private final ReceiptService receiptService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentStatusChangedEvent e) {
        if (e.status() != PaymentStatus.SUCCEEDED) {
            return;
        }

        Payment payment = paymentService.findById(e.paymentId());

        receiptService.issueIfNotExists(
                payment.getUserId(),
                payment.getOrder(),
                payment.getOrderKey(),
                payment.getPaymentKey(),
                payment.getAmount()
        );
    }
}
