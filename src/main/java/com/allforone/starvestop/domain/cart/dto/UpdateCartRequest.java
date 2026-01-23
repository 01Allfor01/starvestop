package com.allforone.starvestop.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCartRequest {
    Long id;
    Integer quantity;
}
