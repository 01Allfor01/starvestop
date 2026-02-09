package com.allforone.starvestop.domain.settlement.entity;

import com.allforone.starvestop.domain.settlement.enums.SettlementAction;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "settlement_audit_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

    @Column(nullable = false)
    private Long actorAdminId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SettlementAction action;

    @Column(length = 1000)
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private SettlementAuditLog(Settlement settlement, Long actorAdminId, SettlementAction action, String reason) {
        this.settlement = settlement;
        this.actorAdminId = actorAdminId;
        this.action = action;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public static SettlementAuditLog of(Settlement settlement, Long actorAdminId, SettlementAction action, String reason) {
        return new SettlementAuditLog(settlement, actorAdminId, action, reason);
    }
}
