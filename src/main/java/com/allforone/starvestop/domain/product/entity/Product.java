package com.allforone.starvestop.domain.product.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "products")
@SQLRestriction("is_deleted = false")
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

    private Product(Store store, String productName, String description, BigDecimal price, BigDecimal salePrice, String status) {
        this.store = store;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.status = ProductStatus.valueOf(status);
    }

    public static Product create(Store store, String productName, String description, BigDecimal price, BigDecimal salePrice, String status) {
        return new Product(store, productName, description, price, salePrice, status);
    }

    public void update(String productName, String description, BigDecimal price, BigDecimal salePrice, String status) {
        this.productName = (this.productName.equals(productName)) ? this.productName : productName;
        this.description = (this.description.equals(description)) ? this.description : description;
        this.price = (this.price.equals(price)) ? this.price : price;
        this.salePrice = (this.salePrice.equals(salePrice)) ? this.salePrice : salePrice;
        this.status = (this.status.equals(ProductStatus.valueOf(status))) ? this.status : ProductStatus.valueOf(status);
    }

}
