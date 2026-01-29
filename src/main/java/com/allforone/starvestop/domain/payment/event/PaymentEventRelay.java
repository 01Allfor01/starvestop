package com.allforone.starvestop.domain.payment.event;

import com.allforone.starvestop.domain.payment.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventRelay {

    private final DomainEventPublisher publisher;

    public PaymentEventRelay(DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void relayFrom(Payment payment) {
        for (DomainEvent e : payment.pullDomainEvents()) {
            publisher.publish(e);
        }
    }
}
