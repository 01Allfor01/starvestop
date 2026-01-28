package com.allforone.starvestop.domain.coupon.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.coupon.dto.request.CreateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.request.UpdateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponDetailResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.UpdateCouponResponse;
import com.allforone.starvestop.domain.coupon.service.CouponFunction;
import com.allforone.starvestop.domain.coupon.service.CouponService;
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
    private final CouponFunction couponFunction;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateCouponResponse>> createCoupon(@RequestBody CreateCouponRequest request) {
        CreateCouponResponse response = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(COUPON_CREATE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<GetCouponResponse>>> getCouponList() {
        List<GetCouponResponse> responseList = couponService.getCouponList();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_LIST_GET_SUCCESS, responseList));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CommonResponse<GetCouponDetailResponse>> getCoupon(@PathVariable Long couponId) {
        GetCouponDetailResponse response = couponService.getCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_DETAIL_GET_SUCCESS, response));
    }

    @PatchMapping("/{couponId}")
    public ResponseEntity<CommonResponse<UpdateCouponResponse>> updateCoupon(
            @PathVariable Long couponId, @RequestBody UpdateCouponRequest request
    ) {
        UpdateCouponResponse response = couponService.updateCoupon(couponId, request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_STATUS_UPDATE_SUCCESS, response));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<CommonResponse<Void>> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(COUPON_DELETE_SUCCESS));
    }
}
