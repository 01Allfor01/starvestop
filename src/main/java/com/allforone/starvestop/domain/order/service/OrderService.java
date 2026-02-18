package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.coupon.entity.UserCoupon;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.dto.UpdateOrderRequest;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.enums.OrderStatus;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductService orderProductService;

    @Transactional
    public Order createOrder(User user, Store store, UserCoupon userCoupon, BigDecimal discountedPrice, BigDecimal amount) {
        String orderKey = UUID.randomUUID().toString();

        return orderRepository.save(Order.create(store, orderKey, user, userCoupon, discountedPrice, amount));
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
    public OrderResponse updateOrderCancel(Long userId, UpdateOrderRequest request) {
        Order order = findOrder(request.getId());
        userCheck(userId, order);
        order.cancel();
        return OrderResponse.from(order);
    }

    @Transactional
    public void deleteOrder(Long userId, Long orderId) {
        Order order = findOrder(orderId);
        userCheck(userId, order);

        List<OrderProduct> orderProductList = orderProductService.findListByOrderId(orderId);

        orderProductList.forEach(OrderProduct::delete);

        order.delete();
    }

    @Transactional
    public void paid(String orderKey) {
        orderRepository.updateStatusToPaidByOrderKey(orderKey);
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

    public Order getById(Long userId) {
        return orderRepository.findByIdAndIsDeletedIsFalse(userId).orElseThrow(
                () -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    public Order getForPayment(Long orderId) {
        return orderRepository.getByIdForUpdate(orderId);
    }

    public Order getForPayment(String orderKey) {
        return orderRepository.getByIdForUpdate(orderKey);
    }
    public List<Order> getExpiredOrder(LocalDateTime now) {
        return orderRepository.findByStatusAndExpiresAtBefore(OrderStatus.PENDING, now);
    }
}
