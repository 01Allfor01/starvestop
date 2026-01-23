package com.allforone.starvestop.domain.order.dto;

import com.allforone.starvestop.domain.order.enums.OrderStatus;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderResponse {
    private final Long id;
    private final Long storeId;
    private final Long userId;
    private final String orderKey;
    private final OrderStatus status;
    private final BigDecimal amount;

    public OrderResponse(Long orderId, Long storeId, Long userId, String orderKey, OrderStatus orderStatus, BigDecimal amount) {
        this.id = orderId;
        this.storeId = storeId;
        this.userId = userId;
        this.orderKey = orderKey;
        this.status = orderStatus;
        this.amount = amount;
    }
}
