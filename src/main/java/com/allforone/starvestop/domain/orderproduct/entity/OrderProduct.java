package com.allforone.starvestop.domain.orderproduct.entity;

import com.allforone.starvestop.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer productPrice;

    private OrderProduct(Order order, Long productId, String productName, Integer quantity, Integer productPrice) {
        this.order = order;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public OrderProduct create(Order order, Long productId, String productName, Integer quantity, Integer productPrice) {
        return new OrderProduct(order, productId, productName, quantity, productPrice);
    }
}
