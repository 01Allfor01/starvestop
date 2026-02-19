package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.response.GetReceiptDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetReceiptResponse;
import com.allforone.starvestop.domain.payment.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Receipts", description = "영수증 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Operation(summary = "영수증 목록 조회" + ApiRoleLabels.AUTH)
    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetReceiptResponse>>> getReceiptList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int pageNum,
            @RequestParam int pageSize) {
        Long userId = authUser.getUserId();

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<GetReceiptResponse> result = receiptService.getReceiptList(userId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.RECEIPT_GET_SUCCESS, result));
    }

    @Operation(summary = "영수증 상세 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/{receiptId}")
    public ResponseEntity<CommonResponse<GetReceiptDetailResponse>> getReceiptList(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long receiptId
    ) {
        Long userId = authUser.getUserId();

        GetReceiptDetailResponse result = receiptService.getReceipt(userId, receiptId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.RECEIPT_GET_SUCCESS, result));
    }
}
