package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderProductResponse;
import com.allforone.starvestop.domain.order.service.OrderProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Order Products", description = "주문 상품 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequestMapping("/order-products")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;

    @Operation(summary = "주문 상품 목록 조회" + ApiRoleLabels.USER)
    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<List<OrderProductResponse>>> getOrderProductList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {
        List<OrderProductResponse> response = orderProductService.getOrderProductList(authUser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(ORDER_PRODUCT_LIST_GET_SUCCESS, response));
    }
}
