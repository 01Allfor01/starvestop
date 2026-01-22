package com.allforone.starvestop.domain.cart.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.cart.dto.CartRequest;
import com.allforone.starvestop.domain.cart.dto.CartResponse;
import com.allforone.starvestop.domain.cart.dto.UpdateCartRequest;
import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.repository.CartRepository;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.repository.ProductRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponse createCart(Long userId, CartRequest request) {
        User user = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Product product = productRepository.findByIdAndIsDeletedIsFalse(request.getProductId()).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)
        );

        Cart cart = Cart.create(user, product, request.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        return new CartResponse(savedCart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getCartList(Long userId) {
        List<Cart> cartList = cartRepository.findALLByUserIdAndIsDeletedIsFalse(userId);

        return cartList.stream().map(CartResponse::new).toList();
    }

    @Transactional
    public CartResponse updateCart(UpdateCartRequest request) {
        Cart cart = cartRepository.findByIdAndIsDeletedIsFalse(request.getCartId()).orElseThrow(
                () -> new CustomException(ErrorCode.CART_NOT_FOUND)
        );
        cart.update(request.getQuantity());
        cartRepository.flush();
        return new CartResponse(cart);
    }
}
