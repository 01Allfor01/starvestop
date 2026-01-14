package com.allforone.starvestop.domain.user.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.user.dto.request.DeleteUserRequest;
import com.allforone.starvestop.domain.user.dto.request.UpdateUserRequest;
import com.allforone.starvestop.domain.user.dto.response.UpdateUserResponse;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.USER_UPDATE_SUCCESS;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원 정보 수정
    @PatchMapping
    public ResponseEntity<CommonResponse<UpdateUserResponse>> updateUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UpdateUserRequest request
    ) {
        Long userId = authUser.getUserId();
        UpdateUserResponse response = userService.updateUser(userId, request);

        CommonResponse<UpdateUserResponse> result = CommonResponse.success(USER_UPDATE_SUCCESS, response);
        return ResponseEntity.ok(result);
    }

    // 회원 탈퇴
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody DeleteUserRequest request
    ) {
        Long userId = authUser.getUserId();
        userService.deleteUser(userId, request);

        return ResponseEntity.ok().body(CommonResponse.successNoData(SuccessMessage.USER_DELETE_SUCCESS));
    }
}
