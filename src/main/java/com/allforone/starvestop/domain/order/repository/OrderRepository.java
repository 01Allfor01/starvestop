package com.allforone.starvestop.domain.order.repository;

import com.allforone.starvestop.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndIsDeletedIsFalse(Long orderId);

    List<Order> findAllByUserIdAndIsDeletedIsFalse(Long userId);
}
