package com.allforone.starvestop.domain.order.service;

import com.allforone.starvestop.domain.cart.entity.Cart;
import com.allforone.starvestop.domain.order.dto.OrderProductResponse;
import com.allforone.starvestop.domain.order.entity.Order;
import com.allforone.starvestop.domain.order.entity.OrderProduct;
import com.allforone.starvestop.domain.order.repository.OrderProductRepository;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    public List<OrderProduct> findListByOrderId(Long orderId) {
        return orderProductRepository.findAllByOrderId(orderId);
    }

    public List<OrderProduct> saveAll(Order order, List<Cart> cartList) {
        List<OrderProduct> orderProductList = cartList.stream()
                .map(cart -> {
                    Product product = cart.getProduct();
                    BigDecimal actualPrice = product.getStatus() == ProductStatus.SALE
                            ? product.getSalePrice()
                            : product.getPrice();

                    return OrderProduct.create(
                            order,
                            product.getId(),
                            product.getName(),
                            cart.getQuantity(),
                            actualPrice
                    );
                }).toList();
        return orderProductRepository.saveAll(orderProductList);
    }
}
