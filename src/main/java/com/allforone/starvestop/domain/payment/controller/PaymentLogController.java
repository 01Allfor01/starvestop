package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogResponse;
import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import com.allforone.starvestop.domain.payment.service.PaymentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-logs")
public class PaymentLogController {
    private final PaymentLogService paymentLogService;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetPaymentLogResponse>>> getPaymentLogList(
            @RequestParam int pageNum, @RequestParam int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<GetPaymentLogResponse> result = paymentLogService.getPaymentLogResponseList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
    }

    @GetMapping("/{paymentLogId}")
    public ResponseEntity<CommonResponse<GetPaymentLogDetailResponse>> getPaymentLog(@PathVariable Long paymentLogId) {
        GetPaymentLogDetailResponse result = paymentLogService.getPaymentLogDetail(paymentLogId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
    }

    // 로그 검색 (관리자만 접근 가능)
    @GetMapping("/search")
    public ResponseEntity<CommonResponse<Page<SearchPaymentLogResponse>>> search(
            @RequestParam(required = false) String orderKey,
            @RequestParam(required = false) Long userId,
            @RequestParam int pageNum,
            @RequestParam int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by("timestamp"));

        Page<SearchPaymentLogResponse> response = paymentLogService.searchPaymentLog(orderKey, userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_SEARCH_SUCCESS, response));
    }
}
