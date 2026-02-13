package com.allforone.starvestop.domain.product.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.dto.SliceResponse;
import com.allforone.starvestop.domain.product.dto.condition.SearchProductCond;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.*;
import com.allforone.starvestop.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //특정 매장 상품 추가
    @PostMapping("/products")
    public ResponseEntity<CommonResponse<CreateProductResponse>> createProduct(@AuthenticationPrincipal AuthUser authUser,
                                                                               @Valid @RequestBody CreateProductRequest request) {
        CreateProductResponse createProductResponse = productService.createProduct(authUser, request);

        CommonResponse<CreateProductResponse> response =
                CommonResponse.success(PRODUCT_CREATE_SUCCESS, createProductResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //특정 매장 상품 목록 조회
    @GetMapping("/stores/{storeId}/products")
    public ResponseEntity<CommonResponse<Slice<GetProductResponse>>> getProductStoreSlice(@PathVariable Long storeId, @PageableDefault(size = 10) Pageable pageable) {
        Slice<GetProductResponse> getProductResponseSlice = productService.getProductStoreSlice(storeId, pageable);

        CommonResponse<Slice<GetProductResponse>> response =
                CommonResponse.success(PRODUCT_LIST_BY_STORE_SUCCESS, getProductResponseSlice);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //마감 세일 상품 목록 조회
    @GetMapping("/products/sale")
    public ResponseEntity<CommonResponse<SliceResponse<GetProductSaleResponse>>> getProductSaleSlice(
            @Valid SearchProductCond request
    ) {
        Slice<GetProductSaleResponse> getProductSaleResponseSlice = productService.getProductSaleSlice(request);

        SliceResponse<GetProductSaleResponse> response = SliceResponse.from(getProductSaleResponseSlice);

        CommonResponse<SliceResponse<GetProductSaleResponse>> result =
                CommonResponse.success(PRODUCT_LIST_BY_SALE_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<GetProductDetailResponse>> getProduct(@PathVariable Long productId) {
        GetProductDetailResponse getProductResponse = productService.getProductDetail(productId);

        CommonResponse<GetProductDetailResponse> response =
                CommonResponse.success(PRODUCT_GET_SUCCESS, getProductResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //특정 매장 상품 수정
    @PatchMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<UpdateProductResponse>> updateProduct(@AuthenticationPrincipal AuthUser authUser,
                                                                               @PathVariable Long productId,
                                                                               @Valid @RequestBody UpdateProductRequest request) {
        UpdateProductResponse updateProductResponse = productService.updateProduct(authUser, productId, request);

        CommonResponse<UpdateProductResponse> response =
                CommonResponse.success(PRODUCT_UPDATE_SUCCESS, updateProductResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 삭제
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<Void>> deleteProduct(@AuthenticationPrincipal AuthUser authUser,
                                                              @PathVariable Long productId) {
        productService.delete(authUser, productId);

        CommonResponse<Void> response = CommonResponse.successNoData(PRODUCT_DELETE_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
