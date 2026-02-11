package com.allforone.starvestop.domain.settlement.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.settlement.dto.request.CreateSettlementRequest;
import com.allforone.starvestop.domain.settlement.dto.response.CreateSettlementResponse;
import com.allforone.starvestop.domain.settlement.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    public ResponseEntity<CommonResponse<CreateSettlementResponse>> create(CreateSettlementRequest request) {
        CreateSettlementResponse response = settlementService.createMonthly(request.getStoreId(), request.getPeriod(), request.getFeeRate());
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SuccessMessage.SETTLEMENT_CREATE_SUCCESS, response));
    }
}
