package com.allforone.starvestop.domain.coupon.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.coupon.dto.request.CreateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.request.UpdateCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponDetailResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.UpdateCouponResponse;
import com.allforone.starvestop.domain.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "Coupons", description = "쿠폰(관리자/조회) API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 생성" + ApiRoleLabels.ADMIN)
    @PostMapping
    public ResponseEntity<CommonResponse<CreateCouponResponse>> createCoupon(@RequestBody CreateCouponRequest request) {
        CreateCouponResponse response = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(COUPON_CREATE_SUCCESS, response));
    }

    @Operation(summary = "쿠폰 목록 조회" + ApiRoleLabels.USER)
    @GetMapping
    public ResponseEntity<CommonResponse<List<GetCouponResponse>>> getCouponList() {
        List<GetCouponResponse> responseList = couponService.getCouponList();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_LIST_GET_SUCCESS, responseList));
    }

    @Operation(summary = "쿠폰 상세 조회" + ApiRoleLabels.USER)
    @GetMapping("/{couponId}")
    public ResponseEntity<CommonResponse<GetCouponDetailResponse>> getCoupon(@PathVariable Long couponId) {
        GetCouponDetailResponse response = couponService.getCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_DETAIL_GET_SUCCESS, response));
    }

    @Operation(summary = "쿠폰 상태 변경" + ApiRoleLabels.ADMIN)
    @PatchMapping("/{couponId}")
    public ResponseEntity<CommonResponse<UpdateCouponResponse>> updateCoupon(
            @PathVariable Long couponId, @RequestBody UpdateCouponRequest request
    ) {
        UpdateCouponResponse response = couponService.updateCoupon(couponId, request);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(COUPON_STATUS_UPDATE_SUCCESS, response));
    }

    @Operation(summary = "쿠폰 삭제" + ApiRoleLabels.ADMIN)
    @DeleteMapping("/{couponId}")
    public ResponseEntity<CommonResponse<Void>> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(COUPON_DELETE_SUCCESS));
    }
}
