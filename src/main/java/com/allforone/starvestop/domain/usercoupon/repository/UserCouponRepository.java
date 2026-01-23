package com.allforone.starvestop.domain.usercoupon.repository;

import com.allforone.starvestop.domain.usercoupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    List<UserCoupon> findAllByUserIdAndIsDeletedIsFalseAndUsedAtIsNull(Long id);

    Optional<UserCoupon> findByIdAndIsDeletedIsFalse(Long id);
}
