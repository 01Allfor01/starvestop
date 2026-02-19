package com.allforone.starvestop.domain.subscription.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.subscription.dto.response.CreateUserSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetUserSubscriptionDetailResponse;
import com.allforone.starvestop.domain.subscription.dto.response.GetUserSubscriptionResponse;
import com.allforone.starvestop.domain.subscription.service.UserSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "User Subscriptions", description = "사용자 구독(중간 테이블) API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-subscriptions")
public class UserSubscriptionController {

    private final UserSubscriptionService userSubscriptionService;

    //사용자 구독 추가
    @Operation(summary = "사용자 구독 추가")
    @PostMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<CommonResponse<CreateUserSubscriptionResponse>> createUserSubscription(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long subscriptionId
    ) {
        CreateUserSubscriptionResponse response = userSubscriptionService.createUserSubscription(authUser, subscriptionId);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.success(USER_SUBSCRIPTION_CREATE_SUCCESS, response));
    }

    //사용자 구독 목록 조회
    @Operation(summary = "내 구독 목록 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<List<GetUserSubscriptionResponse>>> getUserSubscriptions(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser
    ) {
        List<GetUserSubscriptionResponse> responseList = userSubscriptionService.getUserSubscriptions(authUser);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_SUBSCRIPTION_GET_SUCCESS, responseList));
    }

    //사용자 구독 상세 조회
    @Operation(summary = "내 구독 상세 조회")
    @GetMapping("/{userSubscriptionId}")
    public ResponseEntity<CommonResponse<GetUserSubscriptionDetailResponse>> getUserSubscription(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userSubscriptionId
    ) {
        GetUserSubscriptionDetailResponse response = userSubscriptionService.getUserSubscription(authUser, userSubscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(USER_SUBSCRIPTION_GET_DETAIL_SUCCESS, response));
    }

    //사용자 구독 취소
    @Operation(summary = "구독 취소")
    @DeleteMapping("/{userSubscriptionId}")
    public ResponseEntity<CommonResponse<Void>> deleteUserSubscription(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long userSubscriptionId
    ) {
        userSubscriptionService.deleteUserSubscription(authUser, userSubscriptionId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.successNoData(USER_SUBSCRIPTION_CANCEL_SUCCESS));
    }
}
