package com.allforone.starvestop.domain.store.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.store.dto.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.StoreResponse;
import com.allforone.starvestop.domain.store.dto.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.STORE_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<CommonResponse<StoreResponse>> createStore(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateStoreRequest request
    ) {
        StoreResponse response = storeService.createStore(authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(STORE_CREATE_SUCCESS, response));
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<CommonResponse<StoreResponse>> updateStore(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreRequest request
    ) {
        storeService.updateStore(authUser.getUserId(), storeId, request);
    }
}
