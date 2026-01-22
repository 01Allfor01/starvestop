package com.allforone.starvestop.domain.coupon.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.coupon.dto.request.CreateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateCouponResponse;
import com.allforone.starvestop.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.COUPON_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateCouponResponse>> createCoupon(@RequestBody CreateCouponRequest request) {
        CreateCouponResponse response = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(COUPON_CREATE_SUCCESS, response));
    }
}
