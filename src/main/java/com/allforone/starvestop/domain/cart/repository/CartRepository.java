package com.allforone.starvestop.domain.cart.repository;

import com.allforone.starvestop.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("""
            SELECT c FROM Cart c
            WHERE c.user.id = :userId
                AND c.product.store.id = :storeId
            """)
    List<Cart> findAllByUserIdAndStoreId(Long userId, Long storeId);

    void deleteAllByUserId(Long userId);

    @Query("""
            SELECT c FROM Cart c
            WHERE c.user.id = :userId
                AND c.product.store.id = (
                    SELECT c2.product.store.id FROM Cart c2
                    WHERE c2.user.id = :userId
                    ORDER BY c2.createdAt DESC
                    LIMIT 1
                )
            """)
    List<Cart> findAllByUserIdAndLastVisitStore(Long userId);
}
