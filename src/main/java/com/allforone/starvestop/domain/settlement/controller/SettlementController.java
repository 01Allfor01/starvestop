package com.allforone.starvestop.domain.settlement.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.settlement.dto.request.CreateSettlementRequest;
import com.allforone.starvestop.domain.settlement.dto.response.CreateSettlementResponse;
import com.allforone.starvestop.domain.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateSettlementResponse>> create(@RequestBody CreateSettlementRequest request, @AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();

        CreateSettlementResponse response = settlementService.createMonthly(request.getStoreId(), request.getPeriod(), request.getFeeRate(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SuccessMessage.SETTLEMENT_CREATE_SUCCESS, response));
    }
}
