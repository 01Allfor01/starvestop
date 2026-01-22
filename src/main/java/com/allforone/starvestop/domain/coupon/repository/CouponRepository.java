package com.allforone.starvestop.domain.coupon.repository;

import com.allforone.starvestop.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByIsDeletedIsFalse();

    Optional<Coupon> findByIdAndIsDeletedIsFalse(Long couponId);
}
