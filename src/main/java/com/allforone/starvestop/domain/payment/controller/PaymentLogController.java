package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogResponse;
import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import com.allforone.starvestop.domain.payment.service.PaymentLogService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentLogs")
public class PaymentLogController {
    private final PaymentLogService paymentLogService;

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

    // 로그 검색 (관리자만 접근 가능)
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<List<SearchPaymentLogResponse>>> search(
            @RequestParam @Nullable String orderKey,
            @RequestParam @Nullable Long userId) {

        List<SearchPaymentLogResponse> response = paymentLogService.searchPaymentLog(orderKey, userId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_SEARCH_SUCCESS, response));
    }
}
