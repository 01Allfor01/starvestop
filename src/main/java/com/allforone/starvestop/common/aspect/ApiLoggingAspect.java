package com.allforone.starvestop.common.aspect;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.apilog.entity.ApiLog;
import com.allforone.starvestop.domain.apilog.service.ApiLogService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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

    private record UserInfo(String userId, String userName, String userRole) {
    }

    @Pointcut(
            "(within(@org.springframework.web.bind.annotation.RestController *)" +
                    "|| within(@org.springframework.web.bind.annotation.RestControllerAdvice *)) " +
                    "&& !within(com.allforone.starvestop.domain.apilog.controller..*)"
    )
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
            long execTime = System.currentTimeMillis() - startTime;
            if (isAdvice(joinPoint)) {
                isSuccess = false;
                errorMessage = ((CommonResponse<?>) ((ResponseEntity<?>) result).getBody()).getMessage();
                saveLog(isSuccess, errorMessage, execTime);
            } else {
                saveLog(isSuccess, errorMessage, execTime);
            }
        } catch (Exception e) {
            throw e;
        }

        return result;
    }

    private void saveLog(boolean isSuccess, String errorMessage, long execTime) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            UserInfo userInfo = extractUserInfo();

            String clientIp = getClientIp(request);
            String httpMethod = request.getMethod();
            String requestUri = request.getRequestURI();
            String params = request.getQueryString();
            String rawRequestBody = getRequestBody(request);
            String payload = maskPassword(rawRequestBody);

            ApiLog apiLog = ApiLog.create(
                    userInfo.userId(),
                    userInfo.userName(),
                    userInfo.userRole(),
                    clientIp,
                    httpMethod,
                    requestUri,
                    params,
                    payload,
                    isSuccess,
                    errorMessage,
                    execTime
            );

            apiLogService.saveApiLog(apiLog);

        } catch (Exception e) {
            log.error("API 로그 저장 실패. 실패 사유: ", e);
        }
    }

    private boolean isAdvice(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass()
                .isAnnotationPresent(RestControllerAdvice.class);
    }

    private UserInfo extractUserInfo() {
        String userId = null;
        String userName = null;
        String userRole = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof AuthUser authUser) {
                userId = String.valueOf(authUser.getUserId());
                userName = authUser.getUsername();

                if (authUser.getUserRole() != null) {
                    userRole = authUser.getUserRole().name();
                }
            }
        }

        return new UserInfo(userId, userName, userRole);
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

    private String maskPassword(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return jsonString;
        }
        try {
            JsonNode node = objectMapper.readTree(jsonString);
            if (node.isObject()) {
                ObjectNode objectNode = (ObjectNode) node;
                if (objectNode.has("password")) {
                    objectNode.put("password", "*****");
                    return objectMapper.writeValueAsString(objectNode);
                }
            }
        } catch (Exception e) {
            return jsonString;
        }
        return jsonString;
    }
}