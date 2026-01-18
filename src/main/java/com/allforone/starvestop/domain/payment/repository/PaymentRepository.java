package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Slice<Payment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Optional<Payment> findByIdAndIsDeletedIsFalse(Long userId);
}
