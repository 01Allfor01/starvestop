package com.allforone.starvestop.domain.usercoupon.dto.response;

import com.allforone.starvestop.domain.usercoupon.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetUserCouponResponse {

    private final Long id;
    private final Long userId;
    private final Long couponId;
    private final String couponName;
    private final LocalDateTime startedAt;
    private final LocalDateTime expiresAt;

    public static GetUserCouponResponse from(UserCoupon userCoupon) {
        return new GetUserCouponResponse(
                userCoupon.getId(),
                userCoupon.getUser().getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getCoupon().getName(),
                userCoupon.getStartedAt(),
                userCoupon.getExpiresAt()
        );
    }
}
