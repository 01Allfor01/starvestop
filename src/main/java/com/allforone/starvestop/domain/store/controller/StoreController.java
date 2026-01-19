package com.allforone.starvestop.domain.store.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.store.dto.StoreListResponse;
import com.allforone.starvestop.domain.store.dto.StoreRequest;
import com.allforone.starvestop.domain.store.dto.StoreResponse;
import com.allforone.starvestop.domain.store.dto.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<CommonResponse<StoreResponse>> createStore(@Valid @RequestBody StoreRequest request) {
        StoreResponse response = storeService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(STORE_CREATE_SUCCESS, response));
    }

    @PatchMapping("/{storeId}")
    public ResponseEntity<CommonResponse<StoreResponse>> updateStore(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreRequest request
    ) {
        StoreResponse response = storeService.updateStore(authUser.getUserId(), storeId, request);
        return ResponseEntity.ok(CommonResponse.success(STORE_UPDATE_SUCCESS, response));
    }

    @DeleteMapping("/{storeId}")
    public ResponseEntity<CommonResponse<Void>> deleteStore(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId
    ) {
        storeService.deleteStore(authUser.getUserId(), storeId);
        return ResponseEntity.ok(CommonResponse.successNoData(STORE_DELETE_SUCCESS));
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<CommonResponse<StoreResponse>> getStoreDetail(@PathVariable Long storeId) {
        StoreResponse response = storeService.getStoreDetail(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_DETAIL_GET_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<StoreListResponse>>> getStoreList() {
        List<StoreListResponse> response = storeService.getStoreList();
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_LIST_GET_SUCCESS, response));
    }
}