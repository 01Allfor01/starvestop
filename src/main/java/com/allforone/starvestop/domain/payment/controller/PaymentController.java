package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CommonResponse<Void>> create(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreatePaymentRequest request
    ) {
        Long userId = authUser.getUserId();

        paymentService.createPayment(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.successNoData(SuccessMessage.PAYMENT_CREATE_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Slice<GetPaymentResponse>>> getMyPaymentS(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Slice<GetPaymentResponse> response = paymentService.getMyPaymentList(authUser.getUserId(), pageable);

        CommonResponse<Slice<GetPaymentResponse>> result = CommonResponse.success(SuccessMessage.MY_PAYMENT_LIST_GET_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<CommonResponse<GetPaymentDetailsResponse>> getPaymentDetail(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long paymentId
    ) {
        Long userId = authUser.getUserId();
        GetPaymentDetailsResponse response = paymentService.getPayment(userId, paymentId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_DETAIL_GET_SUCCESS, response));
    }
}
