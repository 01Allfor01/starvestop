package com.allforone.starvestop.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "주문 생성 요청")
@Getter
@NoArgsConstructor
public class OrderRequest {
    @Schema(description = "매장 ID", example = "1")
    @NotNull(message = "매장 아이디를 입력해주세요")
    private Long storeId;

    @Schema(description = "사용자 쿠폰 ID (선택)", example = "10", nullable = true)
    private Long userCouponId;
}
