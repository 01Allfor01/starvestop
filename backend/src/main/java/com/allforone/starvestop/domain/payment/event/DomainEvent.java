package com.allforone.starvestop.domain.payment.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime createdAt();
}
