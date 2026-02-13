package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetPaymentLogResponse;
import com.allforone.starvestop.domain.payment.dto.response.SearchPaymentLogResponse;
import com.allforone.starvestop.domain.payment.service.PaymentLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment Logs", description = "결제 로그(관리자) API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentLogs")
public class PaymentLogController {
    private final PaymentLogService paymentLogService;

    @Operation(summary = "결제 로그 목록 조회" + ApiRoleLabels.ADMIN)
    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetPaymentLogResponse>>> getPaymentLogList(
            @RequestParam int pageNum, @RequestParam int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        Page<GetPaymentLogResponse> result = paymentLogService.getPaymentLogResponseList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
    }

    @Operation(summary = "결제 로그 상세 조회" + ApiRoleLabels.ADMIN)
    @GetMapping("/{paymentLogId}")
    public ResponseEntity<CommonResponse<GetPaymentLogDetailResponse>> getPaymentLog(@PathVariable Long paymentLogId) {
        GetPaymentLogDetailResponse result = paymentLogService.getPaymentLogDetail(paymentLogId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.PAYMENT_LOG_GET_SUCCESS, result));
    }

    // 로그 검색 (관리자만 접근 가능)
    @Operation(summary = "결제 로그 검색" + ApiRoleLabels.ADMIN)
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
