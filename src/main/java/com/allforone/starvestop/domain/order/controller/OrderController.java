package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.ORDER_CREATE_SUCCESS;

@RestController("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(@AuthenticationPrincipal AuthUser authuser, @PathVariable Long orderId) {
        OrderResponse response = orderService.createOrder(authuser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(ORDER_CREATE_SUCCESS, response));
    }
}
