package com.allforone.starvestop.domain.store.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.dto.SliceResponse;
import com.allforone.starvestop.domain.store.dto.condition.SearchStoreCond;
import com.allforone.starvestop.domain.store.dto.request.CreateStoreRequest;
import com.allforone.starvestop.domain.store.dto.request.UpdateStoreRequest;
import com.allforone.starvestop.domain.store.dto.response.CreateStoreResponse;
import com.allforone.starvestop.domain.store.dto.response.GetStoreDetailResponse;
import com.allforone.starvestop.domain.store.dto.response.GetStoreForOwnerResponse;
import com.allforone.starvestop.domain.store.dto.response.StoreResponse;
import com.allforone.starvestop.domain.store.service.StoreForOwnerUseCase;
import com.allforone.starvestop.domain.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final StoreForOwnerUseCase storeForOwnerUseCase;

    //매장 추가
    @PostMapping
    public ResponseEntity<CommonResponse<CreateStoreResponse>> createStore(@AuthenticationPrincipal AuthUser authUser,
                                                                           @Valid @RequestBody CreateStoreRequest request) {
        CreateStoreResponse response = storeService.createStore(authUser.getUserId(), authUser.getUserRole(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(STORE_CREATE_SUCCESS, response));
    }

    //매장 정보 수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<CommonResponse<CreateStoreResponse>> updateStore(@AuthenticationPrincipal AuthUser authUser,
                                                                           @PathVariable Long storeId,
                                                                           @Valid @RequestBody UpdateStoreRequest request) {
        CreateStoreResponse response = storeService.updateStore(authUser, storeId, request);

        return ResponseEntity.ok(CommonResponse.success(STORE_UPDATE_SUCCESS, response));
    }

    //매장 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<CommonResponse<Void>> deleteStore(@AuthenticationPrincipal AuthUser authUser,
                                                            @PathVariable Long storeId) {
        storeService.deleteStore(authUser, storeId);

        return ResponseEntity.ok(CommonResponse.successNoData(STORE_DELETE_SUCCESS));
    }

    //매장 조회
    @GetMapping
    public ResponseEntity<CommonResponse<SliceResponse<StoreResponse>>> getStorePage(
            @ModelAttribute @Valid SearchStoreCond request
    ) {
        Slice<StoreResponse> response = storeService.getStoreSlice(request);

        SliceResponse<StoreResponse> result = SliceResponse.from(response);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_LIST_GET_SUCCESS, result));
    }

    //내 매장 조회
    @GetMapping("/my")
    public ResponseEntity<CommonResponse<Page<GetStoreForOwnerResponse>>> getStorePageForOwner(
            @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<GetStoreForOwnerResponse> response = storeForOwnerUseCase.getStorePageForOwner(authUser, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_LIST_GET_SUCCESS, response));
    }

    //매장 상세 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<CommonResponse<GetStoreDetailResponse>> getStoreDetail(@PathVariable Long storeId) {
        GetStoreDetailResponse response = storeService.getStoreDetail(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_DETAIL_GET_SUCCESS, response));
    }
}