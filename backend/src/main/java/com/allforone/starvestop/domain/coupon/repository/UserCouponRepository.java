package com.allforone.starvestop.domain.coupon.repository;

import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>, UserCouponRepositoryCustom {

    @Query("""
SELECT uc FROM UserCoupon uc
JOIN FETCH uc.coupon
WHERE uc.id = :userCouponId
    AND uc.isDeleted = FALSE
    AND uc.usedAt IS NULL
    AND uc.startedAt <= CURRENT_TIMESTAMP
    AND uc.expiresAt >= CURRENT_TIMESTAMP
            """)
    Optional<UserCoupon> findByIdAndUsable(Long userCouponId);

    Optional<UserCoupon> findByIdAndIsDeletedIsFalse(Long userCouponId);
}
