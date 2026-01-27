package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByIdAndIsDeletedIsFalse(Long userId);

    Optional<Payment> findPaymentByOrderKey(String orderOrderKey);

    Payment getPaymentByOrderKey(String orderKey);

    List<Payment> findAllByOrderUserIdAndIsDeletedIsFalseOrderByCreatedAtDesc(Long userId);

    boolean existsByOrderKey(String orderKey);
}
