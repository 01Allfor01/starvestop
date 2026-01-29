package com.allforone.starvestop.domain.cart.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartFunction {

    private final CartRepository cartRepository;

    public Cart getById(Long userId) {
        return cartRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.CART_NOT_FOUND));
    }

    public List<Cart> findAllByUserIdAndStoreId(Long userId, Long storeId) {
        return cartRepository.findAllByUserIdAndStoreId(userId, storeId);
    }

    public void deleteAll(List<Cart> cartList) {
        cartRepository.deleteAll(cartList);
    }
}
