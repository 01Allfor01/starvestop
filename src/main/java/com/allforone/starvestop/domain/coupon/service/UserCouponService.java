package com.allforone.starvestop.domain.coupon.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.dto.request.CreateUserCouponRequest;
import com.allforone.starvestop.domain.coupon.dto.response.CreateUserCouponResponse;
import com.allforone.starvestop.domain.coupon.dto.response.GetUserCouponResponse;
import com.allforone.starvestop.domain.coupon.entity.Coupon;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.allforone.starvestop.domain.coupon.repository.UserCouponRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final UserService userService;
    private final CouponService couponService;

    @Transactional
    public CreateUserCouponResponse createUserCoupon(AuthUser authUser, Long couponId, CreateUserCouponRequest request) {
        User user = userService.getById(authUser.getUserId());

        Coupon coupon = couponService.getById(couponId);

        couponService.decreaseById(couponId);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt;

        if (coupon.getValidDays() != null && coupon.getExpiresAt() != null) {
            LocalDateTime calculatedDate = now.plusDays(coupon.getValidDays());
            expiredAt = coupon.getExpiresAt().isBefore(calculatedDate)
                    ? coupon.getExpiresAt()
                    : calculatedDate;

        } else if (coupon.getValidDays() != null) {
            expiredAt = now.plusDays(coupon.getValidDays());

        } else if (coupon.getExpiresAt() != null) {
            expiredAt = coupon.getExpiresAt();

        } else {
            throw new CustomException(ErrorCode.COUPON_MISSING_EXPIRATION);
        }

        UserCoupon userCoupon = UserCoupon.create(user, coupon, request.getStartedAt(), expiredAt);
        UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

        return CreateUserCouponResponse.from(savedUserCoupon);
    }

    @Transactional(readOnly = true)
    public List<GetUserCouponResponse> getUserCouponList(AuthUser authUser) {
        List<UserCoupon> responseList = userCouponRepository.findActiveCouponList(authUser.getUserId());

        return responseList.stream().map(GetUserCouponResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public GetUserCouponResponse getUserCoupon(AuthUser authUser, Long userCouponId) {
        UserCoupon userCoupon = getUserCouponOrThrow(userCouponId);

        checkPermission(authUser, userCoupon);

        return GetUserCouponResponse.from(userCoupon);
    }

    @Transactional
    public void deleteUserCoupon(AuthUser authUser, Long userCouponId) {
        UserCoupon userCoupon = getUserCouponOrThrow(userCouponId);
        checkPermission(authUser, userCoupon);
        userCoupon.delete();
        userCouponRepository.flush();
    }

    private UserCoupon getUserCouponOrThrow(Long userCouponId) {
        return userCouponRepository.findByIdAndIsDeletedIsFalse(userCouponId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_COUPON_NOT_FOUND)
        );
    }

    private void checkPermission(AuthUser authUser, UserCoupon userCoupon) {
        if (!userCoupon.getUser().getId().equals(authUser.getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    public UserCoupon getByIdAndNotUsed(Long userCouponId) {
        return userCouponRepository.findByIdAndUsable(userCouponId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_COUPON_NOT_FOUND)
        );
    }
}
