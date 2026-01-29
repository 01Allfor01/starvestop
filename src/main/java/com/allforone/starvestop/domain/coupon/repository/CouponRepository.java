package com.allforone.starvestop.domain.coupon.repository;

import com.allforone.starvestop.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByIsDeletedIsFalse();

    Optional<Coupon> findByIdAndIsDeletedIsFalse(Long couponId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Coupon c SET c.stock = c.stock - 1 WHERE c.id = :id AND c.stock > 0")
    int decreaseQuantity(@Param("id") Long id);
}
