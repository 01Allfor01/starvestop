package com.allforone.starvestop.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING, PAID, FAILED, CANCELED;
}
