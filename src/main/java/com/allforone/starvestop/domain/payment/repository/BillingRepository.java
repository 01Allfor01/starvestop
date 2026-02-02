package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.UserBilling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingRepository extends JpaRepository<UserBilling, Long> {
    Optional<UserBilling> findByUserId(Long userId);
}
