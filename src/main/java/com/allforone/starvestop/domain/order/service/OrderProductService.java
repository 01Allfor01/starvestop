package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.domain.order.dto.OrderProductResponse;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.repository.OrderProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    @Transactional(readOnly = true)
    public List<OrderProductResponse> getOrderProductList(Long userId, Long orderId) {
        List<OrderProduct> orderProductResponseList = orderProductRepository.findAllByOrderIdAndUserIdAndIsDeletedIsFalse(userId, orderId);
        return orderProductResponseList.stream().map(OrderProductResponse::from).toList();
    }
}
