package com.allforone.starvestop.domain.apilog.service;


import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.apilog.dto.ApiLogSearchCond;
import com.allforone.starvestop.domain.apilog.dto.GetApiLogDetailResponse;
import com.allforone.starvestop.domain.apilog.dto.GetApiLogResponse;
import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import com.allforone.starvestop.domain.apilog.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveApiLog(ApiLog apiLog) {
        apiLogRepository.save(apiLog);
    }

    @Transactional(readOnly = true)
    public GetApiLogDetailResponse getApiLog(Long apiLogId) {
        ApiLog apiLog = apiLogRepository.findById(apiLogId).orElseThrow(
                () -> new CustomException(ErrorCode.API_LOG_NOT_FOUND)
        );

        return GetApiLogDetailResponse.from(apiLog);
    }

    @Transactional(readOnly = true)
    public Page<GetApiLogResponse> getApiLogPage(ApiLogSearchCond cond, Pageable pageable) {
        Page<ApiLog> apiLogPage = apiLogRepository.search(cond, pageable);
        return apiLogPage.map(GetApiLogResponse::from);
    }
}
