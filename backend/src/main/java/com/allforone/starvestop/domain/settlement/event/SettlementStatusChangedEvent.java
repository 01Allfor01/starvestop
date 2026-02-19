package com.allforone.starvestop.domain.settlement.event;

import com.allforone.starvestop.domain.settlement.enums.SettlementStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SettlementStatusChangedEvent(
        String eventId,
        Long settlementId,
        Long actorAdminId,
        SettlementStatus fromStatus,
        SettlementStatus toStatus,
        String reason,
        LocalDateTime occurredAt
) {
    public static SettlementStatusChangedEvent of(
            Long settlementId,
            Long actorAdminId,
            SettlementStatus fromStatus,
            SettlementStatus toStatus,
            String reason
    ) {
        return new SettlementStatusChangedEvent(
                UUID.randomUUID().toString(),
                settlementId,
                actorAdminId,
                fromStatus,
                toStatus,
                reason,
                LocalDateTime.now()
        );
    }
}
