package com.allforone.starvestop.domain.cart.repository;

import com.allforone.starvestop.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
