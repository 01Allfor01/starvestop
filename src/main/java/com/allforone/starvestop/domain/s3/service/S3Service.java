package com.allforone.starvestop.domain.s3.service;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.service.ProductService;
import com.allforone.starvestop.domain.s3.dto.request.CreateS3PresignedUrlRequest;
import com.allforone.starvestop.domain.s3.dto.response.CreateS3PresignedUrlResponse;
import com.allforone.starvestop.domain.s3.enums.ImageExtension;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import com.allforone.starvestop.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.bucket-name}")
    private String bucket;

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    //PresignedUrl 발급
    public CreateS3PresignedUrlResponse getCreateS3PresignedUrlResponse(String key, String contentType, UUID uuid) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        String url = s3Presigner
                .presignPutObject(presignRequest)
                .url()
                .toExternalForm();
        return new CreateS3PresignedUrlResponse(uuid.toString(), url);
    }

    //S3 이미지 링크 얻기
    public String createPresignedGetUrl(Long id, S3BucketStatus status, String uuid) {
        if(uuid == null) return null;

        String key = getKey(id, status, UUID.fromString(uuid));

        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(1))
                .getObjectRequest(objectRequest)
                .build();

        return s3Presigner
                .presignGetObject(presignRequest)
                .url()
                .toExternalForm();
    }

    //S3 이미지 삭제
    public void deleteImage(Long id, S3BucketStatus status, String uuid) {
        if (uuid == null) return;

        String key = getKey(id, status, UUID.fromString(uuid));

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    //S3 경로 생성
    public String getKey(Long id, S3BucketStatus status, UUID uuid) {
        return status.name() + "/id:" + id + "/" + uuid;
    }
}
