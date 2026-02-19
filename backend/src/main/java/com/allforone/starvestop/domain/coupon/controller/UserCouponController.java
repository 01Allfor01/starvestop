package com.allforone.starvestop.domain.coupon.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.coupon.dto.request.CreateUserCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateUserCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetUserCouponResponse;
import com.allforone.starvestop.domain.coupon.service.UserCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "User Coupons", description = "사용자 쿠폰 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    @Operation(summary = "사용자 쿠폰 등록" + ApiRoleLabels.USER)
    @PostMapping("/coupons/{couponId}/user-coupons")
    public ResponseEntity<CommonResponse<CreateUserCouponResponse>> createUserCoupon(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long couponId,
            @Valid @RequestBody CreateUserCouponRequest request
    ) {
        CreateUserCouponResponse response = userCouponService.createUserCoupon(authUser, couponId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(USER_COUPON_CREATE_SUCCESS, response));
    }

    @Operation(summary = "내 쿠폰 목록 조회" + ApiRoleLabels.USER)
    @GetMapping("/user-coupons")
    public ResponseEntity<CommonResponse<List<GetUserCouponResponse>>> getUserCouponList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser) {
        List<GetUserCouponResponse> responseList = userCouponService.getUserCouponList(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_COUPON_LIST_GET_SUCCESS, responseList));
    }

    @Operation(summary = "내 쿠폰 상세 조회" + ApiRoleLabels.USER)
    @GetMapping("/user-coupons/{userCouponId}")
    public ResponseEntity<CommonResponse<GetUserCouponResponse>> getUserCoupon(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userCouponId
    ) {
        GetUserCouponResponse response = userCouponService.getUserCoupon(authUser, userCouponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_COUPON_DETAIL_GET_SUCCESS, response));
    }

    @Operation(summary = "내 쿠폰 삭제" + ApiRoleLabels.USER)
    @DeleteMapping("/user-coupons/{userCouponId}")
    public ResponseEntity<CommonResponse<Void>> deleteUserCoupon(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userCouponId
    ) {
        userCouponService.deleteUserCoupon(authUser, userCouponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(USER_COUPON_DELETE_SUCCESS));
    }
}
