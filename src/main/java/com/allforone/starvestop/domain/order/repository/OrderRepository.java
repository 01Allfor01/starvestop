package com.allforone.starvestop.domain.order.repository;

import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndIsDeletedIsFalse(Long orderId);

    List<Order> findAllByUserIdAndIsDeletedIsFalse(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :orderId")
    Order getByIdForUpdate(@Param("orderId") Long orderId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.orderKey = :orderKey")
    Order getByIdForUpdate(@Param("orderKey") String orderKey);

    @Modifying
    @Query("UPDATE Order o SET o.status = 'PAID' WHERE o.orderKey = :orderKey")
    void updateStatusToPaidByOrderKey(@Param("orderKey") String orderKey);

    List<Order> findByStatusAndExpiresAtBefore(OrderStatus status, LocalDateTime now);

}
