package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    Slice<Product> findAllByStatusAndIsDeletedIsFalse(ProductStatus status);

    Optional<Product> findByIdAndIsDeletedIsFalse(Long id);

    Slice<Product> findAllByStoreIdAndIsDeletedIsFalseOrderById(Long storeId);
}
