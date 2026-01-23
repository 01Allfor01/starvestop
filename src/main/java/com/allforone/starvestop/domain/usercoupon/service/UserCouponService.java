package com.allforone.starvestop.domain.usercoupon.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.repository.CouponRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import com.allforone.starvestop.domain.usercoupon.dto.request.CreateUserCouponRequest;
import com.allforone.starvestop.domain.usercoupon.dto.response.CreateUserCouponResponse;
import com.allforone.starvestop.domain.usercoupon.entity.UserCoupon;
import com.allforone.starvestop.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public CreateUserCouponResponse createUserCoupon(AuthUser authUser, Long couponId, CreateUserCouponRequest request) {
        User user = userRepository.findByIdAndIsDeletedIsFalse(authUser.getUserId()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Coupon coupon = couponRepository.findByIdAndIsDeletedIsFalse(couponId).orElseThrow(
                () -> new CustomException(ErrorCode.COUPON_NOT_FOUND)
        );

        UserCoupon userCoupon = UserCoupon.create(user, coupon, request.getStartedAt(), request.getExpiredAt());
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        return CreateUserCouponResponse.from(savedUserCoupon);
    }
}
