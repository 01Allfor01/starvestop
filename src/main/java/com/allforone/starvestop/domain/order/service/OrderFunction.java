package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFunction {

    private final OrderRepository orderRepository;

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
}
