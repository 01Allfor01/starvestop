package com.allforone.starvestop.domain.coupon.repository;

import com.allforone.starvestop.domain.coupon.entity.UserCoupon;

import java.util.List;

public interface UserCouponRepositoryCustom {

    List<UserCoupon> findActiveCouponList(Long userId);
}
