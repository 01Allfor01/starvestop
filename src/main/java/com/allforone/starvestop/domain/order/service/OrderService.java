package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
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
    private final OrderProductFunction orderProductFunction;

    @Transactional
    public Order createOrder(User user, Store store, UserCoupon userCoupon, BigDecimal amount) {
        String orderKey = UUID.randomUUID().toString();

        return orderRepository.save(Order.create(store, orderKey, user, userCoupon, amount));
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

        List<OrderProduct> orderProductList = orderProductFunction.findListByOrderId(orderId);

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
