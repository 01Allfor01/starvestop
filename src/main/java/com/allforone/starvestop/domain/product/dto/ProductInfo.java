//package com.allforone.starvestop.domain.product.dto;
//
//import com.allforone.starvestop.domain.product.entity.Product;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.math.BigDecimal;
//
//@Getter
//@AllArgsConstructor
//public class ProductInfo {
//    private final Long productId;
//    private final Long storeId;
//    private final String productName;
//    private final String description;
//    private final Long stock;
//    private final BigDecimal price;
//    private final BigDecimal salePrice;
//
//    public static ProductInfo from(Product product) {
//        return new ProductInfo(
//                product.getId(),
//                product.getStore().getId(),
//                product.getProductName(),
//                product.getDescription(),
//                product.getStock(),
//                product.getPrice(),
//                product.getSalePrice()
//        );
//    }
//}