package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.dto.UpdateOrderRequest;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<Cart> cartList = cartRepository.findAllByUserId(userId);

        List<Cart> cartOrderList = cartList.stream().filter(cart -> cart.getProduct().getStore().getId().equals(storeId)).toList();

        cartOrderList.forEach(cart -> cart.getProduct().decrease(cart.getQuantity()));

        cartRepository.flush();

        BigDecimal amount = cartOrderList.stream()
                .map(cart -> cart.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cart.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderRepository.save(Order.create(store, orderKey, user, amount));

        List<OrderProduct> orderProductList = cartOrderList.stream()
                .map(cart -> OrderProduct.create(
                        order,
                        cart.getProduct().getId(),
                        cart.getProduct().getName(),
                        cart.getQuantity(),
                        cart.getProduct().getPrice()
                )).toList();

        orderProductRepository.saveAll(orderProductList);

        cartRepository.deleteAll(cartList);

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrderList(Long userId) {
        List<Order> orderList = orderRepository.findAllByUserIdAndIsDeletedIsFalse(userId);

        return orderList.stream().map(OrderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long userId, Long orderId) {
        Order order = findOrder(orderId);

        userCheck(userId, order);

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse updateOrder(Long userId, UpdateOrderRequest request) {
        Order order = findOrder(request.getId());
        userCheck(userId, order);
        order.setStatus(request.getStatus());

        orderRepository.flush();

        return OrderResponse.from(order);
    }

    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order order = findOrder(orderId);
        userCheck(userId, order);

        List<OrderProduct> orderProductList = orderProductRepository.findAllByOrderId(orderId);

        orderProductList.forEach(OrderProduct::delete);

        order.delete();
    }

    private void userCheck(Long userId, Order order) {
        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findByIdAndIsDeletedIsFalse(orderId).orElseThrow(
                () -> new CustomException(ErrorCode.ORDER_NOT_FOUND)
        );
    }
}
