package com.allforone.starvestop.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "주문 취소 요청")
@Getter
@NoArgsConstructor
public class UpdateOrderRequest {
    @Schema(description = "주문 ID", example = "100")
    @NotNull(message = "주문 아이디를 입력해주세요")
    private Long id;
}
