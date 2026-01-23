package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.repository.CartRepository;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import com.allforone.starvestop.domain.orderproduct.entity.OrderProduct;
import com.allforone.starvestop.domain.orderproduct.repository.OrderProductRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.repository.StoreRepository;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderResponse createOrder(Long userId, Long storeId) {
        User user = userRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findByIdAndIsDeletedIsFalse(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        String orderKey = UUID.randomUUID().toString();

        List<Cart> cartList = cartRepository.findALLByUserIdAndIsDeletedIsFalse(userId);

        cartList.forEach(cart -> cart.getProduct().decrease(cart.getQuantity()));

        cartRepository.flush();


        BigDecimal amount = cartList.stream()
                .map(cart -> cart.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderRepository.save(Order.create(store, orderKey, user, amount));

        List<OrderProduct> orderProductList = cartList.stream()
                .map(cart -> OrderProduct.create(
                        order,
                        cart.getProduct().getId(),
                        cart.getProduct().getName(),
                        cart.getQuantity(),
                        cart.getProduct().getPrice()
                )).toList();

        orderProductRepository.saveAll(orderProductList);

        cartRepository.deleteAll(cartList);

        return new OrderResponse(
                order.getId(),
                order.getStore().getId(),
                order.getUser().getId(),
                order.getOrderKey(),
                order.getStatus(),
                order.getAmount()
        );
    }
}
