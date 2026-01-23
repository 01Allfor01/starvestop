package com.allforone.starvestop.domain.usercoupon.dto.response;

import com.allforone.starvestop.domain.usercoupon.entity.UserCoupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateUserCouponResponse {

    private final Long id;
    private final Long userId;
    private final Long couponId;
    private final LocalDateTime startedAt;
    private final LocalDateTime expiredAt;
    private final LocalDateTime createdAt;

    public static CreateUserCouponResponse from(UserCoupon userCoupon) {
        return new CreateUserCouponResponse(
                userCoupon.getId(),
                userCoupon.getUser().getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getStartedAt(),
                userCoupon.getExpiredAt(),
                userCoupon.getCreatedAt()
        );
    }
}
