package com.allforone.starvestop.domain.orderproduct.repository;

import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findAllByOrder_Id(Long id);
}
