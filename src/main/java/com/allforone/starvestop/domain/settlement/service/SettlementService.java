package com.allforone.starvestop.domain.settlement.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.settlement.entity.Settlement;
import com.allforone.starvestop.domain.settlement.enums.SettlementStatus;
import com.allforone.starvestop.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Settlement createMonthly(Long storeId, YearMonth period, BigDecimal feeRate) {
        String periodYm = period.toString(); // "2026-02"

        // 1) 중복 생성 방지(선제 체크)
        if (settlementRepository.existsByStoreIdAndPeriodYm(storeId, periodYm)) {
            throw new CustomException(ErrorCode.SETTLEMENT_ALREADY_EXISTS);
        }

        // 2) 기간 계산 [start, end)
        LocalDateTime start = period.atDay(1).atStartOfDay();
        LocalDateTime end = period.plusMonths(1).atDay(1).atStartOfDay();

        // 3) 결제 집계
        PaymentAggregate agg = paymentRepository.aggregateGrossAmountByStoreAndPaidAt(
                storeId, PaymentStatus.SUCCEEDED    , start, end
        );

        BigDecimal gross = nvl(agg == null ? null : agg.grossAmount());
        if (gross.compareTo(BigDecimal.ZERO) <= 0) {
            // 정책 선택: "결제 없으면 정산 생성 안 함"
            throw new CustomException(ErrorCode.SETTLEMENT_NO_TARGET_PAYMENTS);
        }

        // 4) 수수료/정산금 계산
        BigDecimal fee = gross.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal net = gross.subtract(fee).setScale(2, RoundingMode.HALF_UP);

        // 5) 엔티티 생성 & 저장
        Settlement settlement = Settlement.create(
                storeId,
                periodYm,
                gross,
                BigDecimal.ZERO, // refundAmount (이번 버전은 0)
                fee,
                net,
                SettlementStatus.CREATED
        );

        try {
            return settlementRepository.save(settlement);
        } catch (DataIntegrityViolationException e) {
            // 동시 요청으로 유니크 제약 충돌 시 방어
            throw new CustomException(ErrorCode.SETTLEMENT_ALREADY_EXISTS);
        }
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    public record PaymentAggregate(BigDecimal grossAmount) {}
}
