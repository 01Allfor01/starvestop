package com.allforone.starvestop.domain.cart.repository;

import com.allforone.starvestop.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByIdAndIsDeletedIsFalse(Long cartId);

    @Query("""
            SELECT c FROM Cart c
            WHERE c.user.id = :userId
                AND c.product.store.id = :storeId
            """)
    List<Cart> findAllByUserIdAndIsDeletedIsFalse(Long userId, Long storeId);

    List<Cart> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
