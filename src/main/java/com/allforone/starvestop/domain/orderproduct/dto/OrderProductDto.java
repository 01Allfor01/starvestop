package com.allforone.starvestop.domain.orderproduct.dto;

import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderProductDto {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal productPrice;

    public static OrderProductDto from(OrderProduct orderProduct) {
        return new OrderProductDto(
                orderProduct.getId(),
                orderProduct.getProductId(),
                orderProduct.getProductName(),
                orderProduct.getQuantity(),
                orderProduct.getProductPrice()
        );
    }
}
