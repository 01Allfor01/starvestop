package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
    Optional<Payment> findPaymentByOrderKey(String orderOrderKey);

    Page<Payment> findAllByOrderUserId(Long userId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p where p.orderKey = :orderKey")
    Optional<Payment> findByOrderKeyForUpdate(@Param("orderKey") String orderKey);

    @Query("select p from Payment p where p.orderKey = :orderKey")
    Payment getPaymentByOrderKey(@Param("orderKey") String orderKey);

    List<Payment> findByOrderAndStatusIn(Order order, Collection<PaymentStatus> statuses);

}
