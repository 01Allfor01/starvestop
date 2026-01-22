package com.allforone.starvestop.domain.coupon.service;

import com.allforone.starvestop.domain.coupon.dto.request.CreateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateCouponResponse;
import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CreateCouponResponse createCoupon(CreateCouponRequest request) {
        Coupon coupon = Coupon.create(
                request.getCouponName(),
                request.getDiscountAmount(),
                request.getMinAmount(),
                request.getValidDays(),
                request.getExpiresAt(),
                request.getStock()
        );

        Coupon savedCoupon = couponRepository.save(coupon);

        return CreateCouponResponse.from(savedCoupon);
    }
}
