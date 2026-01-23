package com.allforone.starvestop.domain.coupon.dto.request;

import com.allforone.starvestop.domain.coupon.enums.CouponStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCouponRequest {

    private CouponStatus status;
}
