package com.allforone.starvestop.domain.product.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
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
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Product(Store store, String name, String description, BigDecimal price, Long stock, ProductStatus status) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public static Product create(Store store, String name, String description, BigDecimal price, Long stock, ProductStatus status) {
        return new Product(store, name, description, price, stock, status);
    }

    public void decrease(Long count) {
        if (this.stock == 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stock -= count;
    }

    public void increase(Long count) {
        this.stock += count;
    }

    public void update(String productName, String description, Long stock, BigDecimal price, ProductStatus status) {
        this.name = (this.name.equals(productName)) ? this.name : productName;
        this.description = (this.description.equals(description)) ? this.description : description;
        this.stock = (this.stock.equals(stock)) ? this.stock : stock;
        this.price = (this.price.equals(price)) ? this.price : price;
        this.status = (this.status.equals(status)) ? this.status : status;
    }

}
