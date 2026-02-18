package com.allforone.starvestop.domain.order.dto;

import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "주문 응답")
@Getter
@AllArgsConstructor
public class OrderResponse {
    @Schema(example = "1")
    private final Long id;
    @Schema(example = "5")
    private final Long storeId;
    @Schema(example = "10")
    private final Long userId;
    @Schema(example = "ORD-20260215-001")
    private final String orderKey;
    @Schema(example = "PAID")
    private final OrderStatus status;
    private final BigDecimal discountedPrice;
    @Schema(example = "15000")
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
                order.getDiscountedPrice(),
                order.getAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
