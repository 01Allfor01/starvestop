package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderProductResponse;
import com.allforone.starvestop.domain.order.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.ORDER_PRODUCT_LIST_GET_SUCCESS;

@RestController
@RequestMapping("/order-products")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;

    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<List<OrderProductResponse>>> getOrderProductList(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {
        List<OrderProductResponse> response = orderProductService.getOrderProductList(authUser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(ORDER_PRODUCT_LIST_GET_SUCCESS, response));
    }
}
