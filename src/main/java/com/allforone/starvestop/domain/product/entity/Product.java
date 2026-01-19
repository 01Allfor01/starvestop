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
    @Column(name = "product_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal salePrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Product(Store store, String productName, String description, Long stock, BigDecimal price, BigDecimal salePrice, ProductStatus status) {
        this.store = store;
        this.productName = productName;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.salePrice = salePrice;
        this.status = status;
    }

    public static Product create(Store store, String productName, String description, Long stock, BigDecimal price, BigDecimal salePrice, ProductStatus status) {
        return new Product(store, productName, description, stock, price, salePrice, status);
    }

    public void update(String productName, String description, Long stock, BigDecimal price, BigDecimal salePrice, ProductStatus status) {
        this.productName = (this.productName.equals(productName)) ? this.productName : productName;
        this.description = (this.description.equals(description)) ? this.description : description;
        this.stock = (this.stock.equals(stock)) ? this.stock : stock;
        this.price = (this.price.equals(price)) ? this.price : price;
        this.salePrice = (this.salePrice.equals(salePrice)) ? this.salePrice : salePrice;
        this.status = (this.status.equals(status)) ? this.status : status;
    }

}
