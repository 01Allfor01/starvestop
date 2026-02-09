package com.allforone.starvestop.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "settlement_payouts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementPayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal payoutAmount;

    @Column(nullable = false, length = 16)
    private String bankCode;

    @Column(nullable = false, length = 64)
    private String accountNumberMasked;

    @Column(nullable = false, length = 50)
    private String holderName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private PayoutStatus status;

    @Column(length = 1000)
    private String failureReason;

    private LocalDateTime executedAt;

    private SettlementPayout(Settlement settlement,
                             BigDecimal payoutAmount,
                             String bankCode,
                             String accountNumberMasked,
                             String holderName) {
        this.settlement = settlement;
        this.payoutAmount = payoutAmount;
        this.bankCode = bankCode;
        this.accountNumberMasked = accountNumberMasked;
        this.holderName = holderName;
        this.status = PayoutStatus.REQUESTED;
        this.executedAt = LocalDateTime.now();
    }

    public static SettlementPayout requested(Settlement settlement,
                                             BigDecimal payoutAmount,
                                             String bankCode,
                                             String accountNumberMasked,
                                             String holderName) {
        return new SettlementPayout(settlement, payoutAmount, bankCode, accountNumberMasked, holderName);
    }

    public void success() {
        this.status = PayoutStatus.SUCCESS;
    }

    public void fail(String reason) {
        this.status = PayoutStatus.FAILED;
        this.failureReason = reason;
    }

    public enum PayoutStatus {
        REQUESTED, SUCCESS, FAILED
    }
}
