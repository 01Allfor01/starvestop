package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByStatusAndIsDeletedFalse(ProductStatus status);

    List<Product> findAllByStoreAndIsDeletedFalse(Store store);

    Optional<Product> findByIdAndIsDeletedFalse(Long id);
}
