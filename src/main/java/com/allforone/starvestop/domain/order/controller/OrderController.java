package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderRequest;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.dto.UpdateOrderRequest;
import com.allforone.starvestop.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(@AuthenticationPrincipal AuthUser authuser, @RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(authuser.getUserId(), request.getStoreId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(ORDER_CREATE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getOrder(@AuthenticationPrincipal AuthUser authUser) {
        List<OrderResponse> response = orderService.getOrder(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(ORDER_GET_SUCCESS, response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> getOrder(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {
        OrderResponse response = orderService.getOrder(authUser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(ORDER_GET_SUCCESS, response));
    }

    @PatchMapping
    public ResponseEntity<CommonResponse<OrderResponse>> updateOrder(@AuthenticationPrincipal AuthUser authUser, @RequestBody UpdateOrderRequest request) {
        OrderResponse response = orderService.updateOrder(authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(CART_UPDATE_SUCCESS, response));
    }
}
