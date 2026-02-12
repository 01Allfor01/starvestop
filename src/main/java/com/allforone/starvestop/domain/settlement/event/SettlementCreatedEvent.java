package com.allforone.starvestop.domain.settlement.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record SettlementCreatedEvent(
        String eventId,
        Long settlementId,
        Long actorAdminId,
        Long storeId,
        String periodYm,
        LocalDateTime occurredAt
) {
    public static SettlementCreatedEvent of(Long settlementId,Long actorAdminId,Long storeId, String periodYm) {
        return new SettlementCreatedEvent(
                UUID.randomUUID().toString(),
                settlementId,
                actorAdminId,
                storeId,
                periodYm,
                LocalDateTime.now()
        );
    }
}
