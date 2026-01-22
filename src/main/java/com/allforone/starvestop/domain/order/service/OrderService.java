package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderResponse createOrder(Long userId, Long orderId) {
        return null;
    }
}
