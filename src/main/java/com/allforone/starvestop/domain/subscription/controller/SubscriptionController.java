package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.request.UpdateSubscriptionRequest;

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
public class SubscriptionController {

//    private final SubscriptionService subscriptionService;
//
//    // 구독 생성
//    @PostMapping("/stores/{storeId}/subscriptions")
//    public ResponseEntity<CommonResponse<CreateSubscriptionResponse>> createSubscription(
//            @AuthenticationPrincipal AuthUser authUser,
//            @PathVariable Long storeId,
//            @Valid @RequestBody CreateSubscriptionRequest request
//    ) {
//        CreateSubscriptionResponse response = subscriptionService.createSubscription(authUser, storeId, request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SUBSCRIPTION_CREATE_SUCCESS, response));
//    }
//
//    // 전체 구독 목록 조회
//    @GetMapping("/subscriptions")
//    public ResponseEntity<CommonResponse<List<GetSubscriptionResponse>>> getSubscriptionList() {
//        List<GetSubscriptionResponse> responseList = subscriptionService.getSubscriptionList();
//        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, responseList));
//    }
//
//    // 특정 매장 구독 목록 조회
//    @GetMapping("/stores/{storeId}/subscriptions")
//    public ResponseEntity<CommonResponse<List<GetSubscriptionResponse>>> getSubscriptionListByStore(
//            @PathVariable Long storeId
//    ) {
//        List<GetSubscriptionResponse> responseList = subscriptionService.getSubscriptionListByStore(storeId);
//        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, responseList));
//    }
//
//    // 구독 상세 조회
//    @GetMapping("/subscriptions/{subscriptionId}")
//    public ResponseEntity<CommonResponse<GetSubscriptionResponse>> getSubscription(@PathVariable Long subscriptionId) {
//        GetSubscriptionResponse response = subscriptionService.getSubscription(subscriptionId);
//        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, response));
//    }
//
//    @PatchMapping("/subscriptions/{subscriptionId}")
//    public ResponseEntity<CommonResponse<UpdateSubscriptionResponse>> updateSubscription(
//            @PathVariable Long subscriptionId,
//            @Valid @RequestBody UpdateSubscriptionRequest request
//    ) {
//        UpdateSubscriptionResponse response = subscriptionService.updateSubscription(request, subscriptionId);
//        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, response));
//    }
//
//
//    // 구독 삭제
//    @DeleteMapping("/subscriptions/{subscriptionId}")
//    public ResponseEntity<CommonResponse<Void>> deleteSubscription(
//            @AuthenticationPrincipal AuthUser authUser,
//            @PathVariable Long subscriptionId
//    ) {
//        subscriptionService.deleteSubscription(authUser, subscriptionId);
//        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(SUBSCRIPTION_DELETE_SUCCESS));
//    }
}
