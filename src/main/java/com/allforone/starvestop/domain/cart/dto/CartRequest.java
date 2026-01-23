package com.allforone.starvestop.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartRequest {
    @NotNull(message = "상품 아이디를 입력해주세요")
    private Long productId;
    @Min(value = 1, message = "수량이 잘못되었습니다")
    @NotNull(message = "상품 수량을 입력해주세요")
    private Integer quantity;
}
