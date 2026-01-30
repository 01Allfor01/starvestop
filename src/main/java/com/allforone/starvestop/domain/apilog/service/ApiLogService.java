package com.allforone.starvestop.domain.apilog.service;


import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import com.allforone.starvestop.domain.apilog.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
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
}
