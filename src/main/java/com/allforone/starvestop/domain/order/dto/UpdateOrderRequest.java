package com.allforone.starvestop.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateOrderRequest {
    @NotNull(message = "주문 아이디를 입력해주세요")
    private Long id;
}
