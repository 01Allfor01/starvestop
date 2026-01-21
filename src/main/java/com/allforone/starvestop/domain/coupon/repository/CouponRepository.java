package com.allforone.starvestop.domain.coupon.repository;

import com.allforone.starvestop.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
