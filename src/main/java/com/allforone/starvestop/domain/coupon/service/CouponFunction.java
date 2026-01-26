package com.allforone.starvestop.domain.coupon.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponFunction {

    private final CouponRepository couponRepository;

    public Coupon getById(Long id) {
        return couponRepository.findByIdAndIsDeletedIsFalse(id).orElseThrow(
                () -> new CustomException(ErrorCode.COUPON_NOT_FOUND)
        );
    }


}
