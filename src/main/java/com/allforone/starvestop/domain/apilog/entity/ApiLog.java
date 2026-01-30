package com.allforone.starvestop.domain.apilog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "api_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(nullable = false)
    private String httpMethod;

    @Column(nullable = false)
    private String requestUri;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String requestBody;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String responseBody;

    @Column(nullable = false)
    private boolean isSuccess;

    @Column
    private String errorMessage;

    @Column(nullable = false)
    private Long execTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ApiLog(
            String userId,
            String clientIp,
            String httpMethod,
            String requestUri,
            String requestBody,
            String responseBody,
            boolean isSuccess,
            String errorMessage,
            Long execTime
    ) {
        this.userId = userId;
        this.clientIp = clientIp;
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.requestBody = requestBody;
        this.responseBody = responseBody;
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.execTime = execTime;
        this.createdAt = LocalDateTime.now();
    }

    public static ApiLog create(
            String userId,
            String clientIp,
            String httpMethod,
            String requestUri,
            String requestBody,
            String responseBody,
            boolean isSuccess,
            String errorMessage,
            Long execTime
    ) {
        return new ApiLog(
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
    }
}
