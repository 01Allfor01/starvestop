package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.service.PaymentUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentUsecase paymentUsecase;

    @PostMapping
    public ResponseEntity<CommonResponse<CreatePaymentResponse>> create(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreatePaymentRequest request
    ) {
        Long userId = authUser.getUserId();
        Long orderId = request.getOrderId();

        CreatePaymentResponse result = paymentUsecase.createPayment(userId, orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SuccessMessage.PAYMENT_REQUIRE_SUCCESS, result));
    }

    @GetMapping("/success")
    public ResponseEntity<Void> success(
            @RequestParam String paymentType,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount
    ) {
        String redirectPath = paymentUsecase.confirmSuccess(paymentKey, orderId, amount);
        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .location(URI.create(redirectPath))
                .build();
    }

    @GetMapping("/fail")
    public ResponseEntity<Void> fail(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String orderId
    ) {
        String redirectPath = paymentUsecase.failRedirect(code, orderId);
        return ResponseEntity.status(HttpStatus.FOUND) // 302
                .location(URI.create(redirectPath))
                .build();
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetPaymentResponse>>> getMyPaymentList(
            @AuthenticationPrincipal AuthUser authUser, @RequestParam int pageNum, @RequestParam int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending());

        Page<GetPaymentResponse> response = paymentUsecase.getMyPaymentList(authUser.getUserId(), pageable);

        CommonResponse<Page<GetPaymentResponse>> result = CommonResponse.success(SuccessMessage.MY_PAYMENT_LIST_GET_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<GetPaymentDetailsResponse>> getPaymentDetail(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long paymentId
    ) {
        Long userId = authUser.getUserId();
        GetPaymentDetailsResponse response = paymentUsecase.getPayment(userId, paymentId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_DETAIL_GET_SUCCESS, response));
    }
}
