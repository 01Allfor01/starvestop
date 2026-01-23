package com.allforone.starvestop.domain.usercoupon.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.usercoupon.dto.request.CreateUserCouponRequest;
import com.allforone.starvestop.domain.usercoupon.dto.response.CreateUserCouponResponse;
import com.allforone.starvestop.domain.usercoupon.service.UserCouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.USER_COUPON_CREATE_SUCCESS;

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

}
