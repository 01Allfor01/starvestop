package com.allforone.starvestop.domain.order.repository;

import com.allforone.starvestop.domain.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    @Query("""
            SELECT op FROM OrderProduct op
            WHERE op.isDeleted = false
                AND op.order.id = :orderId
                And op.order.user.id = :userId
            """)
    List<OrderProduct> findAllByOrderIdAndUserIdAndIsDeletedIsFalse(Long userId, Long orderId);

    List<OrderProduct> findAllByOrderId(Long orderId);
}
