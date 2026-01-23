package com.allforone.starvestop.domain.cart.repository;

import com.allforone.starvestop.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByIdAndIsDeletedIsFalse(Long cartId);

    List<Cart> findAllByUserIdAndIsDeletedIsFalse(Long userId);

    List<Cart> findAllByUserId(Long userId);
}
