package com.allforone.starvestop.domain.order.dto;

import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private final Long id;
    private final Long storeId;
    private final Long userId;
    private final String orderKey;
    private final OrderStatus status;
    private final BigDecimal amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStore().getId(),
                order.getUser().getId(),
                order.getOrderKey(),
                order.getStatus(),
                order.getAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
