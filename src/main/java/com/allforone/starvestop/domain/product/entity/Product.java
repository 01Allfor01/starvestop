package com.allforone.starvestop.domain.product.entity;

import com.allforone.starvestop.common.entity.BaseEntity;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.enums.ProductStatus;
import com.allforone.starvestop.domain.saleproduct.entity.SaleProduct;
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
    private Integer stock;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private SaleProduct saleProduct;

    private Product(Store store, String name, String description, BigDecimal price, Integer stock, ProductStatus status) {
        this.store = store;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public static Product create(Store store, String name, String description, BigDecimal price, BigDecimal salePrice, Integer stock, ProductStatus status) {
        Product product = new Product(store, name, description, price, stock, status);
        product.saleProduct = SaleProduct.create(product, salePrice);
        return product;
    }

    public void update(String productName, String description, Integer stock, BigDecimal price, BigDecimal salePrice, ProductStatus status) {
        this.name = productName;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.saleProduct.update(salePrice);
        this.status = status;
    }

    public void decrease(Integer count) {
        if (this.stock == 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stock -= count;
    }

    public void increase(Integer count) {
        this.stock += count;
    }
}
