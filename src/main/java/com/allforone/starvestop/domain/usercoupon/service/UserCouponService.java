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
import com.allforone.starvestop.domain.usercoupon.dto.response.GetUserCouponResponse;
import com.allforone.starvestop.domain.usercoupon.entity.UserCoupon;
import com.allforone.starvestop.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        UserCoupon userCoupon = UserCoupon.create(user, coupon, request.getStartedAt(), request.getExpiresAt());
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        return CreateUserCouponResponse.from(savedUserCoupon);
    }

    public List<GetUserCouponResponse> getUserCouponList(AuthUser authUser) {
        List<UserCoupon> responseList = userCouponRepository.findAllByUserIdAndIsDeletedIsFalseAndUsedAtIsNull(authUser.getUserId());

        return responseList.stream().map(GetUserCouponResponse::from).toList();
    }

    public GetUserCouponResponse getUserCoupon(AuthUser authUser, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findByIdAndIsDeletedIsFalse(userCouponId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_COUPON_NOT_FOUND)
        );

        if (!userCoupon.getUser().getId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        
        return GetUserCouponResponse.from(userCoupon);
    }
}
