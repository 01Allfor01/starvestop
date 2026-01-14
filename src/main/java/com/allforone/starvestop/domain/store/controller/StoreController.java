package com.allforone.starvestop.domain.store.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.store.dto.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.CreateStoreResponse;
import com.allforone.starvestop.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public CommonResponse<CreateStoreResponse> createStore(@RequestBody CreateStoreRequest request) {
        CreateStoreResponse response = storeService.createStore(1L, request);
        return CommonResponse.success(SuccessMessage.STORE_CREATE_SUCCESS, response);
    }
}
