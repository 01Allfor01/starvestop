package com.allforone.starvestop.domain.settlement.entity;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.settlement.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(
        name = "settlements",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_settlement_store_period",
                        columnNames = {"store_id", "period_ym"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "period_ym", nullable = false, length = 7)
    private String periodYm;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal grossAmount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal refundAmount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal feeAmount;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SettlementStatus status;

    public static Settlement create(
            Long storeId,
            String periodYm,
            BigDecimal grossAmount,
            BigDecimal refundAmount,
            BigDecimal feeAmount,
            BigDecimal netAmount,
            SettlementStatus status
    ) {
        Settlement settlement = new Settlement();
        settlement.storeId = storeId;
        settlement.periodYm = periodYm;
        settlement.grossAmount = grossAmount;
        settlement.refundAmount = refundAmount;
        settlement.feeAmount = feeAmount;
        settlement.netAmount = netAmount;
        settlement.status = status;
        return settlement;
    }

    public void confirm() {
        validateStatus(SettlementStatus.CREATED);
        this.status = SettlementStatus.CONFIRMED;
    }

    public void complete() {
        validateStatus(SettlementStatus.CONFIRMED);
        this.status = SettlementStatus.COMPLETED;
    }

    private void validateStatus(SettlementStatus expected) {
        if (this.status != expected) {
            throw new CustomException(ErrorCode.INVALID_SETTLEMENT_STATUS_TRANSITION);
        }
    }
}
