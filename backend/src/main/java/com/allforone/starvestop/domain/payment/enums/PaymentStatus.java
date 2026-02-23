package com.allforone.starvestop.domain.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    REQUESTED,
    PENDING,
    SUCCEEDED,

    FAILED_RETRYABLE,
    FAILED_NON_RETRYABLE,

    CANCELED
}
