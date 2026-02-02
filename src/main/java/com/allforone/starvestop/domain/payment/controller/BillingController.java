package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.payment.dto.request.BillingConfirmRequest;
import com.allforone.starvestop.domain.payment.dto.response.BillingConfirmResponse;
import com.allforone.starvestop.domain.payment.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.BILLING_CONFIRM_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<BillingConfirmResponse>> confirm(
            @RequestBody BillingConfirmRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUserId();

        billingService.confirmAndActivate(
                userId,
                request.getCustomerKey(),
                request.getAuthKey(),
                request.getSubscriptionId()
        );

        return ResponseEntity.ok(CommonResponse.success(BILLING_CONFIRM_SUCCESS,new BillingConfirmResponse(true)));
    }
}
