package com.allforone.starvestop.domain.apilog.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.apilog.dto.ApiLogSearchCond;
import com.allforone.starvestop.domain.apilog.dto.GetApiLogDetailResponse;
import com.allforone.starvestop.domain.apilog.dto.GetApiLogResponse;
import com.allforone.starvestop.domain.apilog.service.ApiLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.API_LOG_GET_SUCCESS;

@Tag(name = "Admin - API Logs", description = "API 로그 조회(관리자)")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequestMapping("/api-logs")
@RequiredArgsConstructor
public class ApiLogController {

    private final ApiLogService apiLogService;

    @Operation(summary = "API 로그 상세 조회" + ApiRoleLabels.ADMIN)
    @GetMapping("/{apiLogId}")
    public ResponseEntity<CommonResponse<GetApiLogDetailResponse>> getApiLog(@PathVariable Long apiLogId) {
        GetApiLogDetailResponse response = apiLogService.getApiLog(apiLogId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(API_LOG_GET_SUCCESS, response));
    }

    @Operation(summary = "API 로그 목록 조회(검색/페이징)" + ApiRoleLabels.ADMIN)
    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetApiLogResponse>>> getAllApiLogPage(
            @ModelAttribute ApiLogSearchCond cond,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<GetApiLogResponse> responsePage = apiLogService.getApiLogPage(cond, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(API_LOG_GET_SUCCESS, responsePage));
    }
}
