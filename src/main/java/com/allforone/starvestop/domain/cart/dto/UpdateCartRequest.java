package com.allforone.starvestop.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCartRequest {
    @NotNull(message = "장바구니 아이디를 입력해주세요")
    Long id;
    @Min(value=1, message = "수량이 잘못 입력되었습니다")
    Integer quantity;
}
