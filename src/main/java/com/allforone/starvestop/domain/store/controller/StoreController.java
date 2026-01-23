package com.allforone.starvestop.domain.store.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.request.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.request.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.dto.response.StoreDetailResponse;
import com.allforone.starvestop.domain.store.dto.response.StoreListResponse;
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

    //매장 추가
    @PostMapping
    public ResponseEntity<CommonResponse<StoreDetailResponse>> createStore(@Valid @RequestBody CreateStoreRequest request) {
        StoreDetailResponse response = storeService.createStore(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(STORE_CREATE_SUCCESS, response));
    }

    //매장 정보 수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<CommonResponse<StoreDetailResponse>> updateStore(@AuthenticationPrincipal AuthUser authUser,
                                                                           @PathVariable Long storeId,
                                                                           @Valid @RequestBody UpdateStoreRequest request) {
        StoreDetailResponse response = storeService.updateStore(authUser.getUserId(), storeId, request);

        return ResponseEntity.ok(CommonResponse.success(STORE_UPDATE_SUCCESS, response));
    }

    //매장 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<CommonResponse<Void>> deleteStore(@AuthenticationPrincipal AuthUser authUser,
                                                            @PathVariable Long storeId) {
        storeService.deleteStore(authUser.getUserId(), storeId);

        return ResponseEntity.ok(CommonResponse.successNoData(STORE_DELETE_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<StoreListResponse>>> getStoreList(
            @ModelAttribute @Valid SearchStoreCond request
    ) {
        List<StoreListResponse> response = storeService.getStoreList(request);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_LIST_GET_SUCCESS, response));
    }

    //매장 상세 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<CommonResponse<StoreDetailResponse>> getStoreDetail(@PathVariable Long storeId) {
        StoreDetailResponse response = storeService.getStoreDetail(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_DETAIL_GET_SUCCESS, response));
    }
}