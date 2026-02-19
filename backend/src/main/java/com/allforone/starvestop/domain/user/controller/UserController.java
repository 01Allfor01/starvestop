package com.allforone.starvestop.domain.user.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.user.dto.request.UpdateUserRequest;
import com.allforone.starvestop.domain.user.dto.response.GetUserResponse;
import com.allforone.starvestop.domain.user.dto.response.UpdateUserResponse;
import com.allforone.starvestop.domain.user.service.UserService;
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

import static com.allforone.starvestop.common.enums.SuccessMessage.USER_GET_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.USER_UPDATE_SUCCESS;

@Tag(name = "Users", description = "회원 정보 API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 조회
    @Operation(summary = "사용자 조회" + ApiRoleLabels.USER)
    @GetMapping
    public ResponseEntity<CommonResponse<GetUserResponse>> getUser(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser) {
        Long userId = authUser.getUserId();
        GetUserResponse response = userService.getUser(userId);

        CommonResponse<GetUserResponse> result = CommonResponse.success(USER_GET_SUCCESS, response);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 회원 정보 수정
    @Operation(summary = "사용자 정보 수정" + ApiRoleLabels.USER)
    @PatchMapping
    public ResponseEntity<CommonResponse<UpdateUserResponse>> updateUser(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        Long userId = authUser.getUserId();
        UpdateUserResponse response = userService.updateUser(userId, request);

        CommonResponse<UpdateUserResponse> result = CommonResponse.success(USER_UPDATE_SUCCESS, response);
        return ResponseEntity.ok(result);
    }

    // 회원 탈퇴
    @Operation(summary = "사용자 탈퇴" + ApiRoleLabels.USER)
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUserId();
        userService.deleteUser(userId);

        CommonResponse<Void> result = CommonResponse.successNoData(SuccessMessage.USER_DELETE_SUCCESS);

        return ResponseEntity.ok().body(result);
    }
}
