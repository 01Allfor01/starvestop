package com.allforone.starvestop.domain.order.event;

import com.allforone.starvestop.domain.order.service.OrderService;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.event.PaymentStatusChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final OrderService orderService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onPaymentStatusSuccess(PaymentStatusChangedEvent event) {

        if (event.status() == PaymentStatus.SUCCEEDED) {
            orderService.paid(event.orderKey());
        }
    }
}
