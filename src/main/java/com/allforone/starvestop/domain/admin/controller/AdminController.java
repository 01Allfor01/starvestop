package com.allforone.starvestop.domain.admin.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.enums.SuccessMessage;
import com.allforone.starvestop.domain.admin.service.AdminService;
import com.allforone.starvestop.domain.admin.dto.GetAdminResponse;
import com.allforone.starvestop.domain.admin.dto.UpdateAdminRequest;
import com.allforone.starvestop.domain.admin.dto.UpdateAdminResponse;
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

import static com.allforone.starvestop.common.enums.SuccessMessage.ADMIN_GET_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.ADMIN_UPDATE_SUCCESS;

@Tag(name = "Admins", description = "관리자(Admin) API")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자 정보 조회
    @Operation(summary = "관리자 조회" + ApiRoleLabels.ADMIN)
    @GetMapping
    public ResponseEntity<CommonResponse<GetAdminResponse>> getAdmin(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser) {
        Long adminId = authUser.getUserId();
        GetAdminResponse response = adminService.getAdmin(adminId);

        CommonResponse<GetAdminResponse> result = CommonResponse.success(ADMIN_GET_SUCCESS, response);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 관리자 비밀번호 변경
    @Operation(summary = "관리자 비밀번호 변경" + ApiRoleLabels.ADMIN)
    @PatchMapping
    public ResponseEntity<CommonResponse<UpdateAdminResponse>> updateAdmin(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody UpdateAdminRequest request
    ) {
        Long userId = authUser.getUserId();
        UpdateAdminResponse response = adminService.updateAdmin(userId, request);

        CommonResponse<UpdateAdminResponse> result = CommonResponse.success(ADMIN_UPDATE_SUCCESS, response);
        return ResponseEntity.ok(result);
    }

    // 관리자 탈퇴
    @Operation(summary = "관리자 탈퇴" + ApiRoleLabels.ADMIN)
    @DeleteMapping
    public ResponseEntity<CommonResponse<Void>> deleteAdmin(
            @Parameter(hidden = true) @AuthenticationPrincipal AuthUser authUser
    ) {
        Long adminId = authUser.getUserId();
        adminService.deleteAdmin(adminId);

        CommonResponse<Void> result = CommonResponse.successNoData(SuccessMessage.ADMIN_DELETE_SUCCESS);

        return ResponseEntity.ok().body(result);
    }
}
