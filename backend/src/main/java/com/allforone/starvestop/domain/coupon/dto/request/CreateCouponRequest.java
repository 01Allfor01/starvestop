package com.allforone.starvestop.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "쿠폰 생성 요청")
@Getter
@NoArgsConstructor
public class CreateCouponRequest {

    @Schema(example = "WELCOME_3000")
    @Size(max = 255, message = "쿠폰 이름은 255자 이하로 입력해주세요")
    @NotBlank(message = "쿠폰 이름을 적어주세요")
    private String name;

    @Schema(description = "할인 금액", example = "3000")
    @NotNull(message = "할인 금액을 적어주세요")
    private BigDecimal discountAmount;

    @Schema(description = "최소 주문 금액", example = "15000")
    @NotNull(message = "최소 주문 금액을 적어주세요")
    private BigDecimal minAmount;

    @Schema(description = "유효 일수(선택)", example = "7", nullable = true)
    private Integer validDays;

    @Schema(description = "만료 일시(선택)", example = "2026-03-01T00:00:00", nullable = true)
    private LocalDateTime expiresAt;

    @Schema(description = "수량", example = "100")
    @NotNull(message = "쿠폰 수량을 적어주세요")
    private Integer stock;
}
