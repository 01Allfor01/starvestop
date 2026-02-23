package com.allforone.starvestop.domain.settlement.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.payment.dto.PaymentAggregate;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.payment.repository.PaymentRepository;
import com.allforone.starvestop.domain.settlement.dto.response.CreateSettlementResponse;
import com.allforone.starvestop.domain.settlement.entity.Settlement;
import com.allforone.starvestop.domain.settlement.event.SettlementCreatedEvent;
import com.allforone.starvestop.domain.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher publisher;

    @Transactional
    public CreateSettlementResponse createMonthly(Long storeId, YearMonth period, BigDecimal feeRate, Long actorAdminId) {
        String periodYm = period.toString(); // "2026-02"

        if (settlementRepository.existsByStoreIdAndPeriodYm(storeId, periodYm)) {
            throw new CustomException(ErrorCode.SETTLEMENT_ALREADY_EXISTS);
        }

        LocalDateTime start = period.atDay(1).atStartOfDay();
        LocalDateTime end = period.plusMonths(1).atDay(1).atStartOfDay();

        PaymentAggregate agg = paymentRepository.aggregateGrossAmountByStoreAndPaidAt(
                storeId, PaymentStatus.SUCCEEDED, start, end
        );

        BigDecimal gross = nvl(agg == null ? null : agg.grossAmount());
        if (gross.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ErrorCode.SETTLEMENT_NO_TARGET_PAYMENTS);
        }

        BigDecimal fee = gross.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal net = gross.subtract(fee).setScale(2, RoundingMode.HALF_UP);

        Settlement settlement = Settlement.create(
                storeId,
                periodYm,
                gross,
                BigDecimal.ZERO,
                fee,
                net
        );

        try {
            Settlement result = settlementRepository.save(settlement);

            publisher.publishEvent(SettlementCreatedEvent.of(
                    result.getId(),
                    actorAdminId,
                    result.getStoreId(),
                    result.getPeriodYm()
            ));

            return CreateSettlementResponse.from(result);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.SETTLEMENT_ALREADY_EXISTS);
        }
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
