package com.allforone.starvestop.domain.payment.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    CREATED,
    REQUESTED,
    PENDING,

    SUCCEEDED,
    FAILED,
    CANCELED,

}
