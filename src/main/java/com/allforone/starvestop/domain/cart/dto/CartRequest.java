package com.allforone.starvestop.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "장바구니 담기 요청")
@Getter
@NoArgsConstructor
public class CartRequest {
    @Schema(description = "상품 ID", example = "100")
    @NotNull(message = "상품 아이디를 입력해주세요")
    private Long productId;
    @Schema(description = "수량", example = "2")
    @Min(value = 1, message = "수량이 잘못되었습니다")
    @NotNull(message = "상품 수량을 입력해주세요")
    private Integer quantity;
}
