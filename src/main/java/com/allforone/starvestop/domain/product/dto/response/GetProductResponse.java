//package com.allforone.starvestop.domain.product.dto.response;
//
//import com.allforone.starvestop.domain.product.entity.Product;
//import com.allforone.starvestop.domain.product.enums.ProductStatus;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//
//import java.math.BigDecimal;
//
//@Getter
//@AllArgsConstructor
//public class GetProductResponse {
//    private final Long productId;
//    private final String productName;
//    private final String description;
//    private final Long stock;
//    private final BigDecimal price;
//    private final BigDecimal salePrice;
//    private final ProductStatus status;
//
//    public static GetProductResponse from(Product product) {
//        return new GetProductResponse(
//                product.getId(),
//                product.getProductName(),
//                product.getDescription(),
//                product.getStock(),
//                product.getPrice(),
//                product.getSalePrice(),
//                product.getStatus()
//        );
//    }
//}
