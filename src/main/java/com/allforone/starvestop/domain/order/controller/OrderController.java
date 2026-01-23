package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderRequest;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.ORDER_CREATE_SUCCESS;

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

}
