package com.allforone.starvestop.domain.order.dto;

import com.allforone.starvestop.domain.order.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOrderRequest {
    private Long id;
    private OrderStatus status;
}
