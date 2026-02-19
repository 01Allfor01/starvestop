package com.allforone.starvestop.domain.store.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "Stores", description = "매장 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    private final StoreForOwnerUseCase storeForOwnerUseCase;

    //매장 추가
    @Operation(summary = "매장 추가" + ApiRoleLabels.OWNER_ADMIN)
    @PostMapping
    public ResponseEntity<CommonResponse<CreateStoreResponse>> createStore(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateStoreRequest request) {
        CreateStoreResponse response = storeService.createStore(authUser.getUserId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(STORE_CREATE_SUCCESS, response));
    }

    //매장 정보 수정
    @Operation(summary = "매장 정보 수정" + ApiRoleLabels.OWNER_ADMIN)
    @PatchMapping("/{storeId}")
    public ResponseEntity<CommonResponse<CreateStoreResponse>> updateStore(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody UpdateStoreRequest request) {
        CreateStoreResponse response = storeService.updateStore(authUser, storeId, request);

        return ResponseEntity.ok(CommonResponse.success(STORE_UPDATE_SUCCESS, response));
    }

    //매장 삭제
    @Operation(summary = "매장 삭제" + ApiRoleLabels.OWNER_ADMIN)
    @DeleteMapping("/{storeId}")
    public ResponseEntity<CommonResponse<Void>> deleteStore(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId) {
        storeService.deleteStore(authUser, storeId);

        return ResponseEntity.ok(CommonResponse.successNoData(STORE_DELETE_SUCCESS));
    }

    //매장 조회
    @Operation(summary = "매장 목록 조회 (거리/키워드/카테고리)" + ApiRoleLabels.AUTH)
    @GetMapping
    public ResponseEntity<CommonResponse<SliceResponse<StoreResponse>>> getStorePage(
            @ParameterObject @ModelAttribute @Valid SearchStoreCond request
    ) {
        Slice<StoreResponse> response = storeService.getStoreSlice(request);

        SliceResponse<StoreResponse> result = SliceResponse.from(response);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_LIST_GET_SUCCESS, result));
    }

    //내 매장 조회
    @Operation(summary = "내 매장 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/my")
    public ResponseEntity<CommonResponse<Page<GetStoreForOwnerResponse>>> getStorePageForOwner(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<GetStoreForOwnerResponse> response = storeForOwnerUseCase.getStorePageForOwner(authUser, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_LIST_GET_SUCCESS, response));
    }

    //매장 상세 조회
    @Operation(summary = "매장 상세 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/{storeId}")
    public ResponseEntity<CommonResponse<GetStoreDetailResponse>> getStoreDetail(@PathVariable Long storeId) {
        GetStoreDetailResponse response = storeService.getStoreDetail(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(STORE_DETAIL_GET_SUCCESS, response));
    }
}