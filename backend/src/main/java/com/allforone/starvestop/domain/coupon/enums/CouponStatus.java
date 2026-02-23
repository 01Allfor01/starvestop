package com.allforone.starvestop.domain.coupon.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "쿠폰 상태")
public enum CouponStatus {

    @Schema(description = "사용 가능")
    USABLE,
    @Schema(description = "사용 불가")
    UNUSABLE

}
