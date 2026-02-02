package com.allforone.starvestop.domain.payment.enums;

import lombok.Getter;

@Getter
public enum BillingStatus {
    ACTIVE, EXPIRED, REVOKED, FAILED
}
