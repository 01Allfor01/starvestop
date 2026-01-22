package com.allforone.starvestop.domain.product.repository;

import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p left join fetch p.saleProduct where p.status = :status and p.isDeleted = false")
    List<Product> findAllByStatusAndIsDeletedIsFalse(@Param("status") ProductStatus status);

    @Query("select p from Product p left join fetch p.saleProduct where p.store = :store and p.isDeleted = false")
    List<Product> findAllByStoreAndIsDeletedIsFalse(@Param("store") Store store);

    @EntityGraph(attributePaths = {"saleProduct"})
    Optional<Product> findByIdAndIsDeletedIsFalse(Long id);
}
