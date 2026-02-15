package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.order.dto.OrderProductResponse;
import com.allforone.starvestop.domain.order.dto.OrderProductQuantityDto;
import com.allforone.starvestop.domain.order.entity.Order;
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

    @Transactional(readOnly = true)
    public List<OrderProductQuantityDto> getOrderProductQuantityList(Long orderId) {
        return orderProductRepository.findQuantityAndIdByOrderId(orderId);
    }

    public List<OrderProduct> findListByOrderId(Long orderId) {
        return orderProductRepository.findAllByOrderId(orderId);
    }

    public List<OrderProduct> saveAll(Order order, List<Cart> cartList) {
        List<OrderProduct> orderProductList = cartList.stream()
                .map(cart -> OrderProduct.create(
                        order,
                        cart.getProduct().getId(),
                        cart.getProduct().getName(),
                        cart.getQuantity(),
                        cart.getProduct().getPrice()
                )).toList();
        return orderProductRepository.saveAll(orderProductList);
    }
}
