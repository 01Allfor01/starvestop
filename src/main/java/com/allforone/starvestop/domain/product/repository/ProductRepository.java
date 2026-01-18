package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Slice<Product> findAllByStatusAndIsDeletedFalse(ProductStatus status, Pageable pageable);

    Slice<Product> findAllByStoreAndIsDeletedFalse(Store store, Pageable pageable);

    Optional<Product> findByIdAndIsDeletedFalse(Long id);

}
