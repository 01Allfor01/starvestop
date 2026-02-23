package com.allforone.starvestop.domain.coupon.dto.request;

import com.allforone.starvestop.domain.coupon.enums.CouponStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "쿠폰 상태 변경 요청")
@Getter
@NoArgsConstructor
public class UpdateCouponRequest {

    @Schema(
            description = "쿠폰 상태",
            example = "USABLE",
            allowableValues = {"USABLE", "UNUSABLE"}
    )
    private CouponStatus status;
}
