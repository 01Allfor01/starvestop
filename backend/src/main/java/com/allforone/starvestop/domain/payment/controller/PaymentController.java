package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.service.PaymentUsecase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payments", description = "결제 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentUsecase paymentUsecase;

    @SecurityRequirement(name = OpenApiConfig.BEARER)
    @Operation(summary = "결제 생성" + ApiRoleLabels.USER)
    @PostMapping
    public ResponseEntity<CommonResponse<CreatePaymentResponse>> create(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreatePaymentRequest request
    ) {
        Long userId = authUser.getUserId();
        Long orderId = request.getOrderId();

        CreatePaymentResponse result = paymentUsecase.createPayment(userId, orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SuccessMessage.PAYMENT_REQUIRE_SUCCESS, result));
    }

    @Operation(summary = "결제 성공 콜백 (Redirect)" + ApiRoleLabels.AUTH)
    @GetMapping("/success")
    public ResponseEntity<CommonResponse<Long>> success(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        Long dbOrderId = paymentUsecase.confirmSuccess(paymentKey, orderId, amount);
        return ResponseEntity.ok(CommonResponse.success(SuccessMessage.PAYMENT_REQUIRE_SUCCESS, dbOrderId));
    }

    @Operation(summary = "결제 실패 콜백 (Redirect)" + ApiRoleLabels.AUTH)
    @GetMapping("/fail")
    public ResponseEntity<CommonResponse<Void>> fail(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String orderId
    ) {
        paymentUsecase.failRedirect(code, orderId);
        return ResponseEntity.ok(CommonResponse.exception("결제에 실패했습니다."));
    }

    @SecurityRequirement(name = OpenApiConfig.BEARER)
    @Operation(summary = "내 결제 내역 조회" + ApiRoleLabels.USER)
    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetPaymentResponse>>> getMyPaymentList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int pageNum, @RequestParam int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending());

        Page<GetPaymentResponse> response = paymentUsecase.getMyPaymentList(authUser.getUserId(), pageable);

        CommonResponse<Page<GetPaymentResponse>> result = CommonResponse.success(SuccessMessage.MY_PAYMENT_LIST_GET_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    @SecurityRequirement(name = OpenApiConfig.BEARER)
    @Operation(summary = "내 결제 상세 조회" + ApiRoleLabels.USER)
    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<GetPaymentDetailsResponse>> getPaymentDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long paymentId
    ) {
        Long userId = authUser.getUserId();
        GetPaymentDetailsResponse response = paymentUsecase.getPayment(userId, paymentId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_DETAIL_GET_SUCCESS, response));
    }
}
