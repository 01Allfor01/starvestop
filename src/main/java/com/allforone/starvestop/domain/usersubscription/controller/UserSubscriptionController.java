package com.allforone.starvestop.domain.usersubscription.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.usersubscription.dto.response.CreateUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.dto.response.GetUserSubscriptionResponse;
import com.allforone.starvestop.domain.usersubscription.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    //사용자 구독 추가
    @PostMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<CreateUserSubscriptionResponse>> createUserSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long subscriptionId
    ) {
        CreateUserSubscriptionResponse response = userSubscriptionService.createUserSubscription(authUser, subscriptionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(USER_SUBSCRIPTION_CREATE_SUCCESS, response));
    }

    //사용자 구독 목록 조회
    @GetMapping
    public ResponseEntity<CommonResponse<List<GetUserSubscriptionResponse>>> getUserSubscriptions(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        List<GetUserSubscriptionResponse> responseList = userSubscriptionService.getUserSubscriptions(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_SUBSCRIPTION_GET_SUCCESS, responseList));
    }

    //사용자 구독 상세 조회
    @GetMapping("/{userSubscriptionId}")
    public ResponseEntity<CommonResponse<GetUserSubscriptionResponse>> getUserSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userSubscriptionId
    ) {
        GetUserSubscriptionResponse response = userSubscriptionService.getUserSubscription(authUser, userSubscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_SUBSCRIPTION_GET_DETAIL_SUCCESS, response));
    }

    //사용자 구독 취소
    @DeleteMapping("/{userSubscriptionId}")
    public ResponseEntity<CommonResponse<Void>> deleteUserSubscription(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userSubscriptionId
    ) {
        userSubscriptionService.deleteUserSubscription(authUser, userSubscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(USER_SUBSCRIPTION_CANCEL_SUCCESS));
    }
}
