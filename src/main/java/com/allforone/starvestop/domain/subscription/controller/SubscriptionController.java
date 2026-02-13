package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.request.UpdateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.UpdateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "Subscriptions", description = "구독 상품 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // 구독 생성
    @Operation(summary = "구독 생성" + ApiRoleLabels.OWNER_ADMIN)
    @PostMapping("/stores/{storeId}/subscriptions")
    public ResponseEntity<CommonResponse<CreateSubscriptionResponse>> createSubscription(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        CreateSubscriptionResponse response = subscriptionService.createSubscription(authUser, storeId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SUBSCRIPTION_CREATE_SUCCESS, response));
    }

    // 전체 구독 목록 조회
    @Operation(summary = "전체 구독 목록 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/subscriptions")
    public ResponseEntity<CommonResponse<List<GetSubscriptionResponse>>> getSubscriptionList() {
        List<GetSubscriptionResponse> responseList = subscriptionService.getSubscriptionList();

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, responseList));
    }

    // 특정 매장 구독 목록 조회
    @Operation(summary = "특정 매장 구독 목록 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/stores/{storeId}/subscriptions")
    public ResponseEntity<CommonResponse<List<GetSubscriptionResponse>>> getSubscriptionListByStore(
            @PathVariable Long storeId) {
        List<GetSubscriptionResponse> responseList = subscriptionService.getSubscriptionListByStore(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, responseList));
    }

    // 구독 상세 조회
    @Operation(summary = "구독 상세 조회" + ApiRoleLabels.AUTH)
    @GetMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<GetSubscriptionResponse>> getSubscription(@PathVariable Long subscriptionId) {
        GetSubscriptionResponse response = subscriptionService.getSubscription(subscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, response));
    }

    // 구독 수정
    @Operation(summary = "구독 수정" + ApiRoleLabels.OWNER_ADMIN)
    @PatchMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<UpdateSubscriptionResponse>> updateSubscription(
            @PathVariable Long subscriptionId,
            @Valid @RequestBody UpdateSubscriptionRequest request
    ) {
        UpdateSubscriptionResponse response = subscriptionService.updateSubscription(request, subscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_UPDATE_SUCCESS, response));
    }


    // 구독 삭제
    @Operation(summary = "구독 삭제" + ApiRoleLabels.OWNER_ADMIN)
    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<Void>> deleteSubscription(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long subscriptionId
    ) {
        subscriptionService.deleteSubscription(authUser, subscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(SUBSCRIPTION_DELETE_SUCCESS));
    }
}
