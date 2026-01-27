package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderRequest;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.dto.UpdateOrderRequest;
import com.allforone.starvestop.domain.order.service.OrderService;
import com.allforone.starvestop.domain.order.service.OrderUseCase;
import jakarta.validation.Valid;
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
    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(@AuthenticationPrincipal AuthUser authuser, @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderUseCase.order(authuser.getUserId(), request.getStoreId(), request.getUserCouponId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(ORDER_CREATE_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getOrderList(@AuthenticationPrincipal AuthUser authUser) {
        List<OrderResponse> response = orderService.getOrderList(authUser.getUserId());
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

    @DeleteMapping("/{orderId}")
    public ResponseEntity<CommonResponse<Void>> deleteOrder(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {
        orderService.deleteOrder(authUser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(ORDER_DELETE_SUCCESS));
    }
}
