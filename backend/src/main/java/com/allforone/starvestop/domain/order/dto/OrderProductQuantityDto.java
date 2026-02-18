package com.allforone.starvestop.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductQuantityDto {
    private Long id;
    private Integer quantity;
}
