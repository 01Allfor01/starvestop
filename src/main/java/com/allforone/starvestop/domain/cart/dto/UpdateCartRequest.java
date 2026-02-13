package com.allforone.starvestop.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "장바구니 수량 수정 요청")
@Getter
@NoArgsConstructor
public class UpdateCartRequest {
    @Schema(description = "장바구니 ID", example = "10")
    @NotNull(message = "장바구니 아이디를 입력해주세요")
    Long id;
    @Schema(description = "수량", example = "3")
    @Min(value=1, message = "수량이 잘못 입력되었습니다")
    Integer quantity;
}
