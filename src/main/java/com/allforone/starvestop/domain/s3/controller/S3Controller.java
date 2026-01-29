package com.allforone.starvestop.domain.s3.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.s3.dto.request.CreateS3PresignedUrlRequest;
import com.allforone.starvestop.domain.s3.dto.response.CreateS3PresignedUrlResponse;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.PRESIGNED_URL_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    //사용자 이미지 - url 생성
    @PostMapping("/users/s3")
    public ResponseEntity<CommonResponse<CreateS3PresignedUrlResponse>> createUserPresignedUrl(
            @Valid @RequestBody CreateS3PresignedUrlRequest request) {
        CreateS3PresignedUrlResponse createS3PresignedUrlResponse =
                s3Service.createPresignedUrl(S3BucketStatus.user, request);

        CommonResponse<CreateS3PresignedUrlResponse> response =
                CommonResponse.success(PRESIGNED_URL_CREATE_SUCCESS, createS3PresignedUrlResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //매장 이미지 - url 생성
    @PostMapping("/stores/s3")
    public ResponseEntity<CommonResponse<CreateS3PresignedUrlResponse>> createStorePresignedUrl(
            @Valid @RequestBody CreateS3PresignedUrlRequest request) {
        CreateS3PresignedUrlResponse createS3PresignedUrlResponse =
                s3Service.createPresignedUrl(S3BucketStatus.store, request);

        CommonResponse<CreateS3PresignedUrlResponse> response =
                CommonResponse.success(PRESIGNED_URL_CREATE_SUCCESS, createS3PresignedUrlResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 이미지 - url 생성
    @PostMapping("/products/s3")
    public ResponseEntity<CommonResponse<CreateS3PresignedUrlResponse>> createProductPresignedUrl(
            @Valid @RequestBody CreateS3PresignedUrlRequest request) {
        CreateS3PresignedUrlResponse createS3PresignedUrlResponse =
                s3Service.createPresignedUrl(S3BucketStatus.product, request);

        CommonResponse<CreateS3PresignedUrlResponse> response =
                CommonResponse.success(PRESIGNED_URL_CREATE_SUCCESS, createS3PresignedUrlResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
