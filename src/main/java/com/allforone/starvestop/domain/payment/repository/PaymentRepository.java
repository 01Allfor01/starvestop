package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserIdAndIsDeletedIsFalseOrderByCreatedAtDesc(Long userId);

    Optional<Payment> findByIdAndIsDeletedIsFalse(Long userId);
}
