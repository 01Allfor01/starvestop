package com.allforone.starvestop.domain.orderproduct.repository;

import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
