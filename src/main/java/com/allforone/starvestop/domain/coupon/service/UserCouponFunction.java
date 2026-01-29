package com.allforone.starvestop.domain.coupon.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.allforone.starvestop.domain.coupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCouponFunction {

    private final UserCouponRepository userCouponRepository;

    public UserCoupon getByIdAndNotUsed(Long userCouponId) {
        return userCouponRepository.findByIdAndUsable(userCouponId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_COUPON_NOT_FOUND)
        );
    }
}
