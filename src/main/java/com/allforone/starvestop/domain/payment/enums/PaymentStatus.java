package com.allforone.starvestop.domain.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    REQUESTED,
    PENDING,

    SUCCEEDED,
    FAILED,
    CANCELED,

}
