package com.allforone.starvestop.domain.product.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.CreateProductResponse;
import com.allforone.starvestop.domain.product.dto.response.UpdateProductResponse;
import com.allforone.starvestop.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.PRODUCT_CREATE_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.PRODUCT_UPDATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    //특정 매장 상품 추가
    @PostMapping()
    public ResponseEntity<CommonResponse<CreateProductResponse>> createProduct(@AuthenticationPrincipal AuthUser authUser,
                                                                               @Valid @RequestBody CreateProductRequest request) {
        CreateProductResponse createProductResponse = productService.createProduct(authUser, request);

        CommonResponse<CreateProductResponse> response =
                CommonResponse.success(PRODUCT_CREATE_SUCCESS, createProductResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //특정 매장 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<CommonResponse<UpdateProductResponse>> updateProduct(@AuthenticationPrincipal AuthUser authUser,
                                                                               @PathVariable Long productId,
                                                                               @RequestBody UpdateProductRequest request) {
        UpdateProductResponse updateProductResponse = productService.updateProduct(authUser, productId, request);

        CommonResponse<UpdateProductResponse> response =
                CommonResponse.success(PRODUCT_UPDATE_SUCCESS, updateProductResponse);

        return ResponseEntity.ok(response);
    }

}
