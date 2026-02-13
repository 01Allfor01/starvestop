package com.allforone.starvestop.domain.product.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.dto.SliceResponse;
import com.allforone.starvestop.domain.product.dto.condition.SearchProductCond;
import com.allforone.starvestop.domain.product.dto.request.CreateProductRequest;
import com.allforone.starvestop.domain.product.dto.request.UpdateProductRequest;
import com.allforone.starvestop.domain.product.dto.response.*;
import com.allforone.starvestop.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "Products", description = "상품 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //특정 매장 상품 추가
    @Operation(summary = "특정 매장 상품 추가" + ApiRoleLabels.OWNER)
    @PostMapping("/products")
    public ResponseEntity<CommonResponse<CreateProductResponse>> createProduct(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateProductRequest request) {
        CreateProductResponse createProductResponse = productService.createProduct(authUser, request);

        CommonResponse<CreateProductResponse> response =
                CommonResponse.success(PRODUCT_CREATE_SUCCESS, createProductResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //특정 매장 상품 목록 조회
    @Operation(summary = "특정 매장 상품 목록 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/stores/{storeId}/products")
    public ResponseEntity<CommonResponse<SliceResponse<GetProductResponse>>> getProductStoreSlice(
            @PathVariable Long storeId,
            @RequestParam(required = false) Long lastId,
            @RequestParam(required = false) Integer size) {
        Slice<GetProductResponse> getProductResponseSlice = productService.getProductStoreSlice(storeId, lastId, size);

        SliceResponse<GetProductResponse> response = SliceResponse.from(getProductResponseSlice);

        CommonResponse<SliceResponse<GetProductResponse>> result =
                CommonResponse.success(PRODUCT_LIST_BY_STORE_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //마감 세일 상품 목록 조회
    @Operation(summary = "마감 세일 상품 목록 조회" + ApiRoleLabels.AUTH)
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
    @Operation(summary = "상품 상세 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<GetProductDetailResponse>> getProduct(@PathVariable Long productId) {
        GetProductDetailResponse getProductResponse = productService.getProductDetail(productId);

        CommonResponse<GetProductDetailResponse> response =
                CommonResponse.success(PRODUCT_GET_SUCCESS, getProductResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //특정 매장 상품 수정
    @Operation(summary = "특정 매장 상품 수정" + ApiRoleLabels.OWNER)
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
    @Operation(summary = "상품 삭제" + ApiRoleLabels.OWNER_ADMIN)
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<Void>> deleteProduct(@AuthenticationPrincipal AuthUser authUser,
                                                              @PathVariable Long productId) {
        productService.delete(authUser, productId);

        CommonResponse<Void> response = CommonResponse.successNoData(PRODUCT_DELETE_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
