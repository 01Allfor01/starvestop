package com.allforone.starvestop.domain.receipt.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.receipt.dto.response.GetReceiptDetailResponse;
import com.allforone.starvestop.domain.receipt.dto.response.GetReceiptResponse;
import com.allforone.starvestop.domain.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<GetReceiptResponse>>> getReceiptList(@AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();
        List<GetReceiptResponse> result = receiptService.getReceiptList(userId);

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
