package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.request.UpdateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.UpdateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/store/{storeId}/subscriptions")
    public ResponseEntity<CommonResponse<CreateSubscriptionResponse>> createSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long storeId,
            @Valid @RequestBody CreateSubscriptionRequest request
    ) {
        CreateSubscriptionResponse response = subscriptionService.createSubscription(authUser, storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SUBSCRIPTION_CREATE_SUCCESS, response));
    }

    @PutMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<UpdateSubscriptionResponse>> updateSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long subscriptionId,
            @Valid @RequestBody UpdateSubscriptionRequest request
    ) {
        UpdateSubscriptionResponse response = subscriptionService.updateSubscription(authUser, subscriptionId, request);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(SUBSCRIPTION_UPDATE_SUCCESS, response));
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
