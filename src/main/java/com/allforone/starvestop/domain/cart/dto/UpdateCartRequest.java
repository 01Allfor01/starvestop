package com.allforone.starvestop.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCartRequest {
    Long cartId;
    Integer quantity;
}
