package com.allforone.starvestop.domain.payment.repository;

import com.allforone.starvestop.domain.payment.entity.Payment;
import com.allforone.starvestop.domain.payment.enums.PaymentStatus;
import com.allforone.starvestop.domain.settlement.service.SettlementService;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
    Optional<Payment> findByIdAndIsDeletedIsFalse(Long paymentId);

    Optional<Payment> findPaymentByOrderKey(String orderOrderKey);

    Page<Payment> findAllByOrderUserIdAndIsDeletedIsFalse(Long userId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Payment p where p.orderKey = :orderKey and p.isDeleted = false")
    Optional<Payment> findByOrderKeyForUpdate(@Param("orderKey") String orderKey);

    @Query("select p from Payment p where p.orderKey = :orderKey and p.isDeleted = false")
    Payment getPaymentByOrderKey(@Param("orderKey") String orderKey);

    @Query("""
        select new com.allforone.starvestop.domain.settlement.service.SettlementCreateService.PaymentAggregate(
            coalesce(sum(p.amount), 0)
        )
        from Payment p
        join p.order o
        where o.storeId = :storeId
          and p.status = :status
          and p.paymentAt >= :start
          and p.paymentAt < :end
    """)
    SettlementService.PaymentAggregate aggregateGrossAmountByStoreAndPaidAt(
            @Param("storeId") Long storeId,
            @Param("status") PaymentStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
