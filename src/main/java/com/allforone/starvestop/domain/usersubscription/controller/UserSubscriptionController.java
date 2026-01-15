package com.allforone.starvestop.domain.usersubscription.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.usersubscription.dto.request.CreateUserSubscriptionRequest;
import com.allforone.starvestop.domain.usersubscription.dto.response.CreateUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.service.UserSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.USER_SUBSCRIPTION_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    @PostMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<CreateUserSubscriptionResponse>> createUserSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long subscriptionId,
            @Valid @RequestBody CreateUserSubscriptionRequest request
    ) {
        CreateUserSubscriptionResponse response = userSubscriptionService.createUserSubscription(authUser, subscriptionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(USER_SUBSCRIPTION_CREATE_SUCCESS, response));
    }
}
