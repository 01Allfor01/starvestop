package com.allforone.starvestop.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequest {
    private Long productId;
    private Integer Quantity;
}
