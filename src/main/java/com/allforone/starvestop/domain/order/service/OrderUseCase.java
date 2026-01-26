package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.cart.service.CartFunction;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.product.service.ProductFunction;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreFunction;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private final CartFunction cartFunction;
    private final OrderProductFunction orderProductFunction;
    private final UserFunction userFunction;
    private final StoreFunction storeFunction;
    private final ProductFunction productFunction;
    private final OrderService orderService;

    @Transactional
    public OrderResponse order(Long userId, Long storeId) {
        User user = userFunction.getById(userId);

        Store store = storeFunction.getById(storeId);

        List<Cart> cartList = cartFunction.findAllByUserId(userId);

        List<Cart> cartOrderList = cartList.stream().filter(cart -> cart.getProduct().getStore().getId().equals(storeId)).toList();

        cartOrderList.forEach(cart -> productFunction.decreaseById(cart.getProduct().getId(), cart.getQuantity()));

        BigDecimal amount = calculateAmount(cartList);

        Order order = orderService.createOrder(user, store, amount);

        orderProductFunction.saveAll(order, cartOrderList);

        cartFunction.deleteAll(cartList);

        return OrderResponse.from(order);
    }

    private BigDecimal calculateAmount(List<Cart> cartList) {
        return cartList.stream()
                .map(cart -> cart.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
