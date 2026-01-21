package com.allforone.starvestop.domain.saleproduct.entity;

import com.allforone.starvestop.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "sale_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleProduct {

    @Id
    @Column(name = "product_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private BigDecimal salePrice;
}
