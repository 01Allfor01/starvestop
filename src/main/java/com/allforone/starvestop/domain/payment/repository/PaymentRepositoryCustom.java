package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.dto.PaymentAggregate;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepositoryCustom {
    int markClaimStockRelease(Long paymentId, List<PaymentStatus> allowedStatuses);

    PaymentAggregate aggregateGrossAmountByStoreAndPaidAt(
            Long storeId,
            PaymentStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
}
