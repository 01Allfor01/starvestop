package com.allforone.starvestop.domain.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateUserCouponRequest {

    @NotNull(message = "쿠폰 사용 가능 시간을 적어주세요")
    private LocalDateTime startedAt;
}
