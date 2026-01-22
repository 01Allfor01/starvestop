package com.allforone.starvestop.domain.cart.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.cart.dto.CartRequest;
import com.allforone.starvestop.domain.cart.dto.CartResponse;
import com.allforone.starvestop.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.CART_CREATE_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.CART_GET_SUCCESS;

@RestController("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CommonResponse<CartResponse>> createCart(@AuthenticationPrincipal AuthUser authUser,
                                                                   @Valid @RequestBody CartRequest request) {
        CartResponse response = cartService.createCart(authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(CART_CREATE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CartResponse>>> getCartList(@AuthenticationPrincipal AuthUser authUser) {
        List<CartResponse> response = cartService.getCartList(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(CART_GET_SUCCESS, response));
    }

}
