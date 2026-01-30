package com.allforone.starvestop.domain.apilog.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.apilog.dto.ApiLogSearchCond;
import com.allforone.starvestop.domain.apilog.dto.GetApiLogDetailResponse;
import com.allforone.starvestop.domain.apilog.dto.GetApiLogResponse;
import com.allforone.starvestop.domain.apilog.service.ApiLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.allforone.starvestop.common.enums.SuccessMessage.API_LOG_GET_SUCCESS;

@RestController
@RequestMapping("/api-logs")
@RequiredArgsConstructor
public class ApiLogController {

    private final ApiLogService apiLogService;

    @GetMapping("/{apiLogId}")
    public ResponseEntity<CommonResponse<GetApiLogDetailResponse>> getApiLog(@PathVariable Long apiLogId) {
        GetApiLogDetailResponse response = apiLogService.getApiLog(apiLogId);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(API_LOG_GET_SUCCESS, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<GetApiLogResponse>>> getAllApiLogPage(
            @ModelAttribute ApiLogSearchCond cond,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Page<GetApiLogResponse> responsePage = apiLogService.getApiLogPage(cond, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(API_LOG_GET_SUCCESS, responsePage));
    }
}
