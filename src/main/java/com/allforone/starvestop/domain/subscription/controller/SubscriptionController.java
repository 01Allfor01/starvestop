package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.SUBSCRIPTION_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/store/{storeId}/subscriptions")
    public ResponseEntity<CommonResponse<CreateSubscriptionResponse>> createSubscription(
            @PathVariable Long storeId,
            @Valid @RequestBody CreateSubscriptionRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        CreateSubscriptionResponse response = subscriptionService.createSubscription(storeId, authUser.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SUBSCRIPTION_CREATE_SUCCESS, response));
    }
}
