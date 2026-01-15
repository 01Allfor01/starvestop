package com.allforone.starvestop.domain.product.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
import com.allforone.starvestop.domain.product.dto.response.GetProductSaleResponse;
import com.allforone.starvestop.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.PRODUCT_LIST_BY_SALE_SUCCESS;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //특정 매장 상품 추가
    @PostMapping("/products")
    public ResponseEntity<CommonResponse<CreateProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        CreateProductResponse createProductResponse = productService.createProduct(request);

        CommonResponse<CreateProductResponse> response =
                CommonResponse.success(SuccessMessage.PRODUCT_CREATE_SUCCESS, createProductResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //마감 세일 상품 목록 조회
    @GetMapping("/products/sale")
    public ResponseEntity<CommonResponse<List<GetProductSaleResponse>>> getProductSaleList() {
        List<GetProductSaleResponse> getProductSaleResponseList = productService.getProductSaleList();

        CommonResponse<List<GetProductSaleResponse>> response =
                CommonResponse.success(PRODUCT_LIST_BY_SALE_SUCCESS, getProductSaleResponseList);

        return ResponseEntity.ok(response);
    }
}
