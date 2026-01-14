package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.request.CreateSubscriptionRequest;
import com.allforone.starvestop.domain.subscription.dto.response.CreateSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.service.SubscriptionService;
import com.allforone.starvestop.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.SUBSCRIPTION_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateSubscriptionResponse>> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request,
            User authUser
    ) {
        CreateSubscriptionResponse response = subscriptionService.createSubscription(authUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(SUBSCRIPTION_CREATE_SUCCESS, response));
    }
}
