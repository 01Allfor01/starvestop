package com.allforone.starvestop.domain.coupon.dto.response;

import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.enums.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetCouponDetailResponse {
    private final Long id;
    private final String name;
    private final BigDecimal discountAmount;
    private final BigDecimal minAmount;
    private final Integer validDays;
    private final LocalDateTime expiresAt;
    private final CouponStatus status;
    private final Integer stock;
    private final LocalDateTime updatedAt;

    public static GetCouponDetailResponse from(Coupon coupon) {
        return new GetCouponDetailResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountAmount(),
                coupon.getMinAmount(),
                coupon.getValidDays(),
                coupon.getExpiresAt(),
                coupon.getStatus(),
                coupon.getStock(),
                coupon.getUpdatedAt()
        );
    }
}
