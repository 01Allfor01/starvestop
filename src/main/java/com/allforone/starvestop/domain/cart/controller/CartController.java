package com.allforone.starvestop.domain.cart.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.cart.dto.CartRequest;
import com.allforone.starvestop.domain.cart.dto.CartResponse;
import com.allforone.starvestop.domain.cart.dto.UpdateCartRequest;
import com.allforone.starvestop.domain.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "Carts", description = "장바구니 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "장바구니 담기" + ApiRoleLabels.USER)
    @PostMapping
    public ResponseEntity<CommonResponse<CartResponse>> createCart(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CartRequest request) {
        CartResponse response = cartService.createCart(authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(CART_CREATE_SUCCESS, response));
    }

    @Operation(summary = "매장별 장바구니 조회" + ApiRoleLabels.USER)
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<CommonResponse<List<CartResponse>>> getCartListStore(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @PathVariable Long storeId) {
        List<CartResponse> response = cartService.getCartListStore(authUser.getUserId(), storeId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(CART_GET_SUCCESS, response));
    }

    @Operation(summary = "내 장바구니 조회" + ApiRoleLabels.USER)
    @GetMapping
    public ResponseEntity<CommonResponse<List<CartResponse>>> getCartList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser) {
        List<CartResponse> response = cartService.getCartList(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(CART_GET_SUCCESS, response));
    }

    @Operation(summary = "장바구니 수량 수정" + ApiRoleLabels.USER)
    @PatchMapping
    public ResponseEntity<CommonResponse<CartResponse>> updateCart(@Valid @RequestBody UpdateCartRequest request) {
        CartResponse response = cartService.updateCart(request);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(CART_UPDATE_SUCCESS, response));
    }

    @Operation(summary = "장바구니 항목 삭제" + ApiRoleLabels.USER)
    @DeleteMapping("/{cartId}")
    public ResponseEntity<CommonResponse<Void>> deleteCart(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @PathVariable Long cartId) {
        cartService.deleteCart(authUser.getUserId(), cartId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(CART_DELETE_SUCCESS));
    }

    @Operation(summary = "장바구니 전체 비우기" + ApiRoleLabels.USER)
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteAllCart(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser) {
        cartService.deleteAllCart(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(CART_DELETE_SUCCESS));
    }
}
