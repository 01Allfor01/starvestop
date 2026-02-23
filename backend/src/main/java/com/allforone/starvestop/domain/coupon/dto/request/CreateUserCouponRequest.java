package com.allforone.starvestop.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "사용자 쿠폰 등록 요청")
@Getter
@NoArgsConstructor
public class CreateUserCouponRequest {

    @Schema(description = "사용 시작 시간", example = "2026-02-13T12:00:00")
    @NotNull(message = "쿠폰 사용 가능 시간을 적어주세요")
    private LocalDateTime startedAt;
}
