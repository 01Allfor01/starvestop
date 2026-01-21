package com.allforone.starvestop.domain.order.repository;

import com.allforone.starvestop.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
