package com.allforone.starvestop.domain.receipt.controller;

import com.allforone.starvestop.domain.receipt.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptRepository receiptRepository;
}
