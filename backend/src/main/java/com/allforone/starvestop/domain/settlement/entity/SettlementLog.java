package com.allforone.starvestop.domain.settlement.entity;

import com.allforone.starvestop.domain.settlement.enums.SettlementStatus;
import com.allforone.starvestop.domain.settlement.event.SettlementCreatedEvent;
import com.allforone.starvestop.domain.settlement.event.SettlementStatusChangedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "settlement_logs",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_settlement_log_event",
                columnNames = "event_id"
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SettlementLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "settlement_id", nullable = false)
    private Long settlementId;

    @Column(name = "event_id", nullable = false, length = 64)
    private String eventId;

    @Column(nullable = false)
    private Long actorAdminId;

    @Column(nullable = false, length = 20)
    private String fromStatus;

    @Column(nullable = false, length = 20)
    private String toStatus;

    @Column(length = 1000)
    private String reason;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static SettlementLog from(SettlementCreatedEvent e) {
        return new SettlementLog(
                null,
                e.settlementId(),
                e.eventId(),
                null,
                null,
                SettlementStatus.CREATED.name(),
                "SETTLEMENT_CREATED",
                e.occurredAt(),
                LocalDateTime.now()
        );
    }


    public static SettlementLog from(SettlementStatusChangedEvent e) {
        return new SettlementLog(
                null,
                e.settlementId(),
                e.eventId(),
                e.actorAdminId(),
                e.fromStatus().name(),
                e.toStatus().name(),
                e.reason(),
                e.occurredAt(),
                LocalDateTime.now()
        );
    }
}
