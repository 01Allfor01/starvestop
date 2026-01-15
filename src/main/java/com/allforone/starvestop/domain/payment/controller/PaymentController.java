package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.request.CreatePaymentRequest;
import com.allforone.starvestop.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
