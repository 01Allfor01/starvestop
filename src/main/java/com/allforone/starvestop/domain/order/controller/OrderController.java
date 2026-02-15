package com.allforone.starvestop.domain.order.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.order.dto.OrderRequest;
import com.allforone.starvestop.domain.order.dto.OrderResponse;
import com.allforone.starvestop.domain.order.dto.UpdateOrderRequest;
import com.allforone.starvestop.domain.order.service.OrderService;
import com.allforone.starvestop.domain.order.service.OrderUseCase;
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

@Tag(name = "Orders", description = "주문 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderUseCase orderUseCase;

    @Operation(summary = "주문 생성" + ApiRoleLabels.USER)
    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authuser, @Valid @RequestBody OrderRequest request) {
        OrderResponse response = orderUseCase.order(authuser.getUserId(), request.getStoreId(), request.getUserCouponId());
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(ORDER_CREATE_SUCCESS, response));
    }

    @Operation(summary = "내 주문 목록 조회" + ApiRoleLabels.USER)
    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getOrderList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser) {
        List<OrderResponse> response = orderService.getOrderList(authUser.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(ORDER_GET_SUCCESS, response));
    }

    @Operation(summary = "주문 상세 조회" + ApiRoleLabels.USER)
    @GetMapping("/{orderId}")
    public ResponseEntity<CommonResponse<OrderResponse>> getOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {
        OrderResponse response = orderService.getOrder(authUser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(ORDER_GET_SUCCESS, response));
    }

    @Operation(summary = "주문 취소" + ApiRoleLabels.USER)
    @PatchMapping("/cancel")
    public ResponseEntity<CommonResponse<OrderResponse>> updateOrderCancel(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody UpdateOrderRequest request) {
        OrderResponse response = orderService.updateOrderCancel(authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(CART_UPDATE_SUCCESS, response));
    }

    @Operation(summary = "주문 삭제" + ApiRoleLabels.USER)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<CommonResponse<Void>> deleteOrder(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser, @PathVariable Long orderId) {
        orderService.deleteOrder(authUser.getUserId(), orderId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(ORDER_DELETE_SUCCESS));
    }
}
