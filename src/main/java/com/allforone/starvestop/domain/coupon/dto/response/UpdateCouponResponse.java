package com.allforone.starvestop.domain.coupon.dto.response;

import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UpdateCouponResponse {

    private final Long id;
    private final String name;
    private final CouponStatus couponStatus;
    private final LocalDateTime updatedAt;

    public static UpdateCouponResponse from(Coupon coupon) {
        return new UpdateCouponResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getStatus(),
                coupon.getUpdatedAt()
        );
    }
}
