package com.allforone.starvestop.domain.settlement.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SettlementCreatedEvent(
        String eventId,
        Long settlementId,
        Long storeId,
        String periodYm,
        LocalDateTime occurredAt
) {
    public static SettlementCreatedEvent of(Long settlementId, Long storeId, String periodYm) {
        return new SettlementCreatedEvent(
                UUID.randomUUID().toString(),
                settlementId,
                storeId,
                periodYm,
                LocalDateTime.now()
        );
    }
}
