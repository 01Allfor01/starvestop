package com.allforone.starvestop.domain.usercoupon.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.usercoupon.dto.request.CreateUserCouponRequest;
import com.allforone.starvestop.domain.usercoupon.dto.response.CreateUserCouponResponse;
import com.allforone.starvestop.domain.usercoupon.dto.response.GetUserCouponResponse;
import com.allforone.starvestop.domain.usercoupon.service.UserCouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    @PostMapping("/coupons/{couponId}/user-coupons")
    public ResponseEntity<CommonResponse<CreateUserCouponResponse>> createUserCoupon(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long couponId,
            @Valid @RequestBody CreateUserCouponRequest request
    ) {
        CreateUserCouponResponse response = userCouponService.createUserCoupon(authUser, couponId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(USER_COUPON_CREATE_SUCCESS, response));
    }

    @GetMapping("/user-coupons/me")
    public ResponseEntity<CommonResponse<List<GetUserCouponResponse>>> getUserCouponList(@AuthenticationPrincipal AuthUser authUser) {
        List<GetUserCouponResponse> responseList = userCouponService.getUserCouponList(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_COUPON_LIST_GET_SUCCESS, responseList));
    }

    @GetMapping("/user-coupons/{userCouponId}")
    public ResponseEntity<CommonResponse<GetUserCouponResponse>> getUserCoupon(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userCouponId
    ) {
        GetUserCouponResponse response = userCouponService.getUserCoupon(authUser, userCouponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_COUPON_DETAIL_GET_SUCCESS, response));
    }

    @DeleteMapping("/user-coupons/{userCouponId}")
    public ResponseEntity<CommonResponse<Void>> deleteUserCoupon(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userCouponId
    ) {
        userCouponService.deleteUserCoupon(authUser, userCouponId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(USER_COUPON_DELETE_SUCCESS));
    }
}
