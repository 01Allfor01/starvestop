package com.allforone.starvestop.domain.admin;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.ADMIN_GET_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.ADMIN_UPDATE_SUCCESS;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 회원 조회
    @GetMapping
    public ResponseEntity<CommonResponse<GetAdminResponse>> getAdmin(@AuthenticationPrincipal AuthUser authUser) {
        Long adminId = authUser.getUserId();
        GetAdminResponse response = adminService.getAdmin(adminId);

        CommonResponse<GetAdminResponse> result = CommonResponse.success(ADMIN_GET_SUCCESS, response);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 관리자 비밀번호 변경
    @PatchMapping
    public ResponseEntity<CommonResponse<UpdateAdminResponse>> updateAdmin(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateAdminRequest request
    ) {
        Long userId = authUser.getUserId();
        UpdateAdminResponse response = adminService.updateAdmin(userId, request);

        CommonResponse<UpdateAdminResponse> result = CommonResponse.success(ADMIN_UPDATE_SUCCESS, response);
        return ResponseEntity.ok(result);
    }

    // 관리자 탈퇴
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteAdmin(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        Long adminId = authUser.getUserId();
        adminService.deleteAdmin(adminId);

        CommonResponse<Void> result = CommonResponse.successNoData(SuccessMessage.ADMIN_DELETE_SUCCESS);

        return ResponseEntity.ok().body(result);
    }
}
