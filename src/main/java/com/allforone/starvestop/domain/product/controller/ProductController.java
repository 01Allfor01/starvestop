package com.allforone.starvestop.domain.product.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
import com.allforone.starvestop.domain.product.dto.response.GetProductResponse;
import com.allforone.starvestop.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.PRODUCT_GET_SUCCESS;

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

    //상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<GetProductResponse>> getProduct(@PathVariable Long productId) {
        GetProductResponse getProductResponse = productService.getProduct(productId);

        CommonResponse<GetProductResponse> response =
                CommonResponse.success(PRODUCT_GET_SUCCESS, getProductResponse);

        return ResponseEntity.ok(response);
    }
}
