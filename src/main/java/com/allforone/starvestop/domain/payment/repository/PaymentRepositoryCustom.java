package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentRepositoryCustom {
    int markFailedAndClaimStockRelease(
            Long paymentId,
            PaymentStatus failedStatus,
            List<PaymentStatus> releasableStatuses
    );

    PaymentStatus getStatusByOrderKey(String string);
}
