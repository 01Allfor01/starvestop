package com.allforone.starvestop.domain.coupon.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.dto.request.CreateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.request.UpdateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponDetailResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.UpdateCouponResponse;
import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    public List<GetCouponResponse> getCouponList() {
        List<Coupon> couponList = couponRepository.findAllByIsDeletedIsFalse();

        return couponList.stream().map(GetCouponResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetCouponDetailResponse getCoupon(Long couponId) {
        Coupon coupon = getCouponOrThrow(couponId);

        return GetCouponDetailResponse.from(coupon);
    }

    @Transactional
    public UpdateCouponResponse updateCoupon(Long couponId, UpdateCouponRequest request) {
        Coupon coupon = getCouponOrThrow(couponId);
        coupon.update(request.getCouponStatus());
        couponRepository.flush();
        return UpdateCouponResponse.from(coupon);
    }

    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = getCouponOrThrow(couponId);
        coupon.delete();
        couponRepository.flush();
    }

    private Coupon getCouponOrThrow(Long couponId) {
        return couponRepository.findByIdAndIsDeletedIsFalse(couponId).orElseThrow(
                () -> new CustomException(ErrorCode.COUPON_NOT_FOUND)
        );
    }
}
