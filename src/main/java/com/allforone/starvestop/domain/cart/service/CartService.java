package com.allforone.starvestop.domain.cart.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.cart.dto.CartRequest;
import com.allforone.starvestop.domain.cart.dto.CartResponse;
import com.allforone.starvestop.domain.cart.dto.UpdateCartRequest;
import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.repository.CartRepository;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.service.ProductService;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserFunction userFunction;
    private final ProductService productService;

    @Transactional
    public CartResponse createCart(Long userId, CartRequest request) {
        User user = userFunction.getById(userId);

        Product product = productService.getById(request.getProductId());

        Cart cart = Cart.create(user, product, request.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        return new CartResponse(savedCart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getCartListStore(Long userId, Long storeId) {
        List<Cart> cartList = cartRepository.findAllByUserIdAndStoreId(userId, storeId);

        return cartList.stream().map(CartResponse::new).toList();
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getCartList(Long userId) {
        List<Cart> cartList = cartRepository.findAllByUserIdAndLastVisitStore(userId);
        return cartList.stream().map(CartResponse::new).toList();
    }

    @Transactional
    public CartResponse updateCart(UpdateCartRequest request) {
        Cart cart = getCart(request.getId());
        cart.update(request.getQuantity());
        cartRepository.flush();
        return new CartResponse(cart);
    }

    @Transactional
    public void deleteCart(Long userId, Long cartId) {
        Cart cart = getCart(cartId);
        if (!cart.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        cartRepository.delete(cart);
    }

    private Cart getCart(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(
                () -> new CustomException(ErrorCode.CART_NOT_FOUND)
        );
    }

    @Transactional
    public void deleteAllCart(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }
}
