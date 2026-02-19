package com.allforone.starvestop.domain.s3.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.s3.dto.request.CreateS3PresignedUrlRequest;
import com.allforone.starvestop.domain.s3.dto.response.CreateS3PresignedUrlResponse;
import com.allforone.starvestop.domain.s3.usecase.S3UseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.PRESIGNED_URL_CREATE_SUCCESS;

@Tag(name = "S3", description = "Presigned URL 발급")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3UseCase s3UseCase;

    //사용자 이미지 - url 생성
    @Operation(summary = "사용자 이미지 Presigned URL 생성" + ApiRoleLabels.USER)
    @PostMapping("/users/s3")
    public ResponseEntity<CommonResponse<CreateS3PresignedUrlResponse>> createUserPresignedUrl(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateS3PresignedUrlRequest request) {
        CreateS3PresignedUrlResponse createS3PresignedUrlResponse =
                s3UseCase.createPresignedUrlUser(authUser, request);

        CommonResponse<CreateS3PresignedUrlResponse> response =
                CommonResponse.success(PRESIGNED_URL_CREATE_SUCCESS, createS3PresignedUrlResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //매장 이미지 - url 생성
    @Operation(summary = "매장 이미지 Presigned URL 생성" + ApiRoleLabels.OWNER_ADMIN)
    @PostMapping("/stores/s3")
    public ResponseEntity<CommonResponse<CreateS3PresignedUrlResponse>> createStorePresignedUrl(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateS3PresignedUrlRequest request) {
        CreateS3PresignedUrlResponse createS3PresignedUrlResponse =
                s3UseCase.createPresignedUrlStore(authUser, request);

        CommonResponse<CreateS3PresignedUrlResponse> response =
                CommonResponse.success(PRESIGNED_URL_CREATE_SUCCESS, createS3PresignedUrlResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 이미지 - url 생성
    @Operation(summary = "상품 이미지 Presigned URL 생성" + ApiRoleLabels.OWNER)
    @PostMapping("/products/s3")
    public ResponseEntity<CommonResponse<CreateS3PresignedUrlResponse>> createProductPresignedUrl(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CreateS3PresignedUrlRequest request) {
        CreateS3PresignedUrlResponse createS3PresignedUrlResponse =
                s3UseCase.createPresignedUrlProduct(authUser,  request);
        CommonResponse<CreateS3PresignedUrlResponse> response =
                CommonResponse.success(PRESIGNED_URL_CREATE_SUCCESS, createS3PresignedUrlResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
