package com.allforone.starvestop.domain.payment.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.payment.dto.request.BillingConfirmRequest;
import com.allforone.starvestop.domain.payment.dto.response.BillingConfirmResponse;
import com.allforone.starvestop.domain.payment.dto.response.GetBillingKeyResponse;
import com.allforone.starvestop.domain.payment.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.BILLING_CONFIRM_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.BILLING_GET_SUCCESS;

@Tag(name = "Billing", description = "정기결제(빌링) API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    @Operation(summary = "정기 결제 등록" + ApiRoleLabels.USER)
    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<BillingConfirmResponse>> confirm(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @RequestBody BillingConfirmRequest request
    ) {
        Long userId = authUser.getUserId();

        billingService.confirmAndActivate(
                userId,
                request.getAuthKey(),
                request.getSubscriptionId()
        );

        return ResponseEntity.ok(CommonResponse.success(BILLING_CONFIRM_SUCCESS, new BillingConfirmResponse(true)));
    }

    @Operation(summary = "정기 결제 수단 조회" + ApiRoleLabels.USER)
    @GetMapping
    public ResponseEntity<CommonResponse<GetBillingKeyResponse>> getMyBillingKey(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUserId();
        GetBillingKeyResponse response = billingService.getBillingKey(userId);

        return ResponseEntity.ok(CommonResponse.success(BILLING_GET_SUCCESS, response));
    }
}
