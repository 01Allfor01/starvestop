package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.payment.dto.response.GetReceiptDetailResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetReceiptResponse;
import com.allforone.starvestop.domain.payment.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetReceiptResponse>>> getReceiptList(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int pageNum,
            @RequestParam int pageSize) {
        Long userId = authUser.getUserId();

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<GetReceiptResponse> result = receiptService.getReceiptList(userId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.RECEIPT_GET_SUCCESS, result));
    }

    @GetMapping("/{receiptId}")
    public ResponseEntity<CommonResponse<GetReceiptDetailResponse>> getReceiptList(@AuthenticationPrincipal AuthUser authUser,
                                                                                   @PathVariable Long receiptId
    ) {
        Long userId = authUser.getUserId();

        GetReceiptDetailResponse result = receiptService.getReceipt(userId, receiptId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SuccessMessage.RECEIPT_GET_SUCCESS, result));
    }
}
