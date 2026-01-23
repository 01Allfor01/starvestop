package com.allforone.starvestop.domain.paymentlog.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.paymentlog.dto.GetPaymentLogDetailResponse;
import com.allforone.starvestop.domain.paymentlog.dto.GetPaymentLogResponse;
import com.allforone.starvestop.domain.paymentlog.service.PaymentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentLogs")
public class PaymentLogController {
    private final PaymentLogService paymentLogService;

//    @GetMapping
//    public ResponseEntity<CommonResponse<List<GetPaymentLogResponse>>> getPaymentLogListByUserId(
//            @RequestParam long userId
//    ) {
//        List<GetPaymentLogResponse> result = paymentLogService.getPaymentLogListByUserId(userId);
//
//        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
//    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<GetPaymentLogResponse>>> getPaymentLogList() {
        List<GetPaymentLogResponse> result = paymentLogService.getPaymentLogResponseList();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
    }

    @GetMapping("/{paymentLogId}")
    public ResponseEntity<CommonResponse<GetPaymentLogDetailResponse>> getPaymentLog(@PathVariable Long paymentLogId) {
        GetPaymentLogDetailResponse result = paymentLogService.getPaymentLogDetail(paymentLogId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
    }
}
