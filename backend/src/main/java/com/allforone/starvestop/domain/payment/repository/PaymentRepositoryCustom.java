package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;

import java.util.List;

public interface PaymentRepositoryCustom {
    int markFailedAndClaimStockRelease(
            Long paymentId,
            PaymentStatus failedStatus,
            List<PaymentStatus> releasableStatuses
    );

}
