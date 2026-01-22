package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.request.ConfirmPaymentRequest;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.dto.request.FailPaymentRequest;
import com.allforone.starvestop.domain.payment.dto.response.CreatePaymentResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentDetailsResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentResponse;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreatePaymentResponse>> create(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CreatePaymentRequest request
    ) {
        Long userId = authUser.getUserId();

        CreatePaymentResponse result = paymentService.createPayment(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SuccessMessage.PAYMENT_REQUIRE_SUCCESS,result));
    }

    @GetMapping("/success")
    public void successPayment(@ModelAttribute ConfirmPaymentRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getOrderId());
        String redirectUrl = paymentService.successPayment(request);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/fail")
    public void failPayment(@ModelAttribute FailPaymentRequest request, HttpServletResponse response) throws IOException {

        String redirectUrl = "/fail.html";
        response.sendRedirect(redirectUrl);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<GetPaymentResponse>>> getMyPaymentList(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<GetPaymentResponse> response = paymentService.getMyPaymentList(authUser.getUserId());

        CommonResponse<List<GetPaymentResponse>> result = CommonResponse.success(SuccessMessage.MY_PAYMENT_LIST_GET_SUCCESS, response);

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
