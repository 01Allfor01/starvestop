package com.allforone.starvestop.domain.paymentlog.controller;

import com.allforone.starvestop.domain.paymentlog.service.PaymentLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentLogController {
    private final PaymentLogService paymentLogService;
}
