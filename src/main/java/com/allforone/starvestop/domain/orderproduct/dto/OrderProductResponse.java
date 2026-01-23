package com.allforone.starvestop.domain.orderproduct.dto;

import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderProductResponse {
    private final Long id;
    private final Long orderId;
    private final String productName;
    private final Integer quantity;
    private final BigDecimal productPrice;

    public static OrderProductResponse from(OrderProduct orderProduct) {
        return new  OrderProductResponse(
                orderProduct.getId(),
                orderProduct.getOrder().getId(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getProductPrice()
        );
    }
}
