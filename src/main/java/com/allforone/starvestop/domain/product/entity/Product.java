package com.allforone.starvestop.domain.product.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal salePrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Product(Store store, String productName, String description, BigDecimal price, BigDecimal salePrice, ProductStatus status) {
        this.store = store;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.status = status;
    }

    public static Product create(Store store, String productName, String description, BigDecimal price, BigDecimal salePrice, ProductStatus status) {
        return new Product(store, productName, description, price, salePrice, status);
    }

}
