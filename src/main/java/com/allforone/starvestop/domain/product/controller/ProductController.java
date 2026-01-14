package com.allforone.starvestop.domain.product.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
import com.allforone.starvestop.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //특정 매장 상품 추가
    @PostMapping()
    public ResponseEntity<CommonResponse<CreateProductResponse>> create(@Valid @RequestBody CreateProductRequest request) {

        CreateProductResponse createProductResponse = productService.create(request);

        CommonResponse<CreateProductResponse> response =
                CommonResponse.success(SuccessMessage.PRODUCT_CREATE_SUCCESS, createProductResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
