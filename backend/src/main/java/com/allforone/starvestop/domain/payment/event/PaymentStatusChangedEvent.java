package com.allforone.starvestop.domain.payment.event;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentStatusChangedEvent(
        Long paymentId,
        String orderKey,
        Long userId,
        PaymentStatus status,
        String payload,
        LocalDateTime createdAt
) implements DomainEvent {
    public static PaymentStatusChangedEvent of(
            Long paymentId,
            String orderKey,
            Long userId,
            PaymentStatus status,
            String payload
    ) {
        return new PaymentStatusChangedEvent(
                paymentId, orderKey, userId, status, payload, LocalDateTime.now()
        );
    }
}
