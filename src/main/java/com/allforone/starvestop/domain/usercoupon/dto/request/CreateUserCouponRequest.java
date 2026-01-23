package com.allforone.starvestop.domain.usercoupon.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateUserCouponRequest {

    private LocalDateTime startedAt;
    private LocalDateTime expiredAt;
}
