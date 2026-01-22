package com.allforone.starvestop.domain.coupon.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.coupon.dto.request.CreateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponDetailResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponResponse;
import com.allforone.starvestop.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateCouponResponse>> createCoupon(@Valid @RequestBody CreateCouponRequest request) {
        CreateCouponResponse response = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(COUPON_CREATE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<GetCouponResponse>>> getCoupons() {
        List<GetCouponResponse> responseList = couponService.getCoupons();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_LIST_GET_SUCCESS, responseList));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CommonResponse<GetCouponDetailResponse>> getCoupon(@PathVariable Long couponId) {
        GetCouponDetailResponse response = couponService.getCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_DETAIL_GET_SUCCESS, response));
    }
}
