package com.allforone.starvestop.domain.apilog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "api_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userId;

    @Column
    private String userEmail;

    @Column
    private String userRole;

    @Column
    private String clientIp;

    @Column(nullable = false)
    private String httpMethod;

    @Column(nullable = false)
    private String requestUri;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String params;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String payload;

    @Column(nullable = false)
    private boolean isSuccess;

    @Column
    private String errorMessage;

    @Column(nullable = false)
    private Long execTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public ApiLog(
            String userId,
            String userEmail,
            String userRole,
            String clientIp,
            String httpMethod,
            String requestUri,
            String params,
            String payload,
            boolean isSuccess,
            String errorMessage,
            Long execTime
    ) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.clientIp = clientIp;
        this.httpMethod = httpMethod;
        this.requestUri = requestUri;
        this.params = params;
        this.payload = payload;
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.execTime = execTime;
        this.createdAt = LocalDateTime.now();
    }

    public static ApiLog create(
            String userId,
            String userEmail,
            String userRole,
            String clientIp,
            String httpMethod,
            String requestUri,
            String params,
            String payload,
            boolean isSuccess,
            String errorMessage,
            Long execTime
    ) {
        return new ApiLog(
                userId,
                userEmail,
                userRole,
                clientIp,
                httpMethod,
                requestUri,
                params,
                payload,
                isSuccess,
                errorMessage,
                execTime
        );
    }
}
