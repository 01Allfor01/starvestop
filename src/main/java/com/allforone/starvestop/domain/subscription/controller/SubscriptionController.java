package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/stores/{storeId}/subscriptions")
    public ResponseEntity<CommonResponse<CreateSubscriptionResponse>> createSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        CreateSubscriptionResponse response = subscriptionService.createSubscription(authUser, storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SUBSCRIPTION_CREATE_SUCCESS, response));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<CommonResponse<Slice<GetSubscriptionResponse>>> getSubscriptionList(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Slice<GetSubscriptionResponse> responseList = subscriptionService.getSubscriptionList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, responseList));
    }

    @GetMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<GetSubscriptionResponse>> getSubscription(@PathVariable Long subscriptionId) {
        GetSubscriptionResponse response = subscriptionService.getSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, response));
    }

    @GetMapping("/stores/{storeId}/subscriptions")
    public ResponseEntity<CommonResponse<Slice<GetSubscriptionResponse>>> getSubscriptionListByStore(
            @PathVariable Long storeId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Slice<GetSubscriptionResponse> responseList = subscriptionService.getSubscriptionListByStore(storeId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_GET_SUCCESS, responseList));
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<Void>> deleteSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long subscriptionId
    ) {
        subscriptionService.deleteSubscription(authUser, subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(SUBSCRIPTION_DELETE_SUCCESS));
    }
}
