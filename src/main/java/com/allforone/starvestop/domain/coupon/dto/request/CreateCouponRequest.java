package com.allforone.starvestop.domain.coupon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateCouponRequest {

    @Size(max = 255, message = "쿠폰 이름은 255자 이하로 입력해주세요")
    @NotBlank(message = "쿠폰 이름을 적어주세요")
    private String name;

    @NotNull(message = "할인 금액을 적어주세요")
    private BigDecimal discountAmount;

    @NotNull(message = "최소 주문 금액을 적어주세요")
    private BigDecimal minAmount;

    private Integer validDays;

    private LocalDateTime expiresAt;

    @NotNull(message = "쿠폰 수량을 적어주세요")
    private Integer stock;
}
