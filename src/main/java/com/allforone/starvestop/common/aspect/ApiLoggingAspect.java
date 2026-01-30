package com.allforone.starvestop.common.aspect;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import com.allforone.starvestop.domain.apilog.service.ApiLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ApiLogService apiLogService;
    private final ObjectMapper objectMapper;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {
    }

    @Around("restControllerPointcut()")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        String errorMessage = null;
        boolean isSuccess = true;

        try {
            result = joinPoint.proceed();

            if (result != null) {
                CommonResponse<?> body = (CommonResponse<?>) ((ResponseEntity<?>) result).getBody();

                if (body != null) {
                    isSuccess = body.isSuccess();
                    if (!isSuccess) {
                        errorMessage = body.getMessage();
                    }
                }
            }

            return result;

        } catch (Exception e) {
            isSuccess = false;
            errorMessage = e.getMessage();
            throw e;
        } finally {
            long execTime = System.currentTimeMillis() - startTime;
            saveLog(result, isSuccess, errorMessage, execTime);
        }
    }

    private void saveLog(Object result, boolean isSuccess, String errorMessage, long execTime) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            String userId = getUserId();
            String clientIp = getClientIp(request);
            String httpMethod = request.getMethod();
            String requestUri = request.getRequestURI();
            String requestBody = getRequestBody(request);
            String responseBody = (result != null) ? objectMapper.writeValueAsString(result) : null;

            ApiLog apiLog = ApiLog.create(
                    userId,
                    clientIp,
                    httpMethod,
                    requestUri,
                    requestBody,
                    responseBody,
                    isSuccess,
                    errorMessage,
                    execTime
            );

            apiLogService.saveApiLog(apiLog);

        } catch (Exception e) {
            log.error("API 로그 저장 실패. 실패 사유: ", e);
        }
    }

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getRequestBody(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = null;
        if (request instanceof ContentCachingRequestWrapper) {
            wrapper = (ContentCachingRequestWrapper) request;
        }

        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    return "Request Body 파싱 실패";
                }
            }
        }
        return null;
    }
}