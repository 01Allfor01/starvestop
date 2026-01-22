package com.allforone.starvestop.domain.saleproduct.repository;

import com.allforone.starvestop.domain.saleproduct.entity.SaleProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {
}
