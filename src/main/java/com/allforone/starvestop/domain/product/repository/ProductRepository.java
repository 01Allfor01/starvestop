package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByStatus(ProductStatus status);
    List<Product> findAllByStore(Store store);
}
