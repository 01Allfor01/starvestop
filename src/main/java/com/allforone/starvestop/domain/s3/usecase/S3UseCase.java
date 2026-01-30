package com.allforone.starvestop.domain.s3.usecase;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import com.allforone.starvestop.domain.product.entity.Product;
import com.allforone.starvestop.domain.product.service.ProductService;
import com.allforone.starvestop.domain.s3.dto.request.CreateS3PresignedUrlRequest;
import com.allforone.starvestop.domain.s3.dto.response.CreateS3PresignedUrlResponse;
import com.allforone.starvestop.domain.s3.enums.ImageExtension;
import com.allforone.starvestop.domain.s3.enums.S3BucketStatus;
import com.allforone.starvestop.domain.s3.service.S3Service;
import com.allforone.starvestop.domain.store.entity.Store;
import com.allforone.starvestop.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3UseCase {

    private final S3Service s3Service;
    private final StoreService storeService;
    private final ProductService productService;

    //PresignedUrl 생성
    public CreateS3PresignedUrlResponse createPresignedUrlUser(AuthUser authUser, CreateS3PresignedUrlRequest request) {
        if (!authUser.getUserId().equals(request.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        UUID uuid = UUID.randomUUID();

        String fileExtension = request.getFilename().substring(request.getFilename().lastIndexOf('.') + 1);

        String contentType = ImageExtension.from(fileExtension);

        String key = s3Service.getKey(request.getId(), S3BucketStatus.USER, uuid);

        return s3Service.getCreateS3PresignedUrlResponse(key, contentType, uuid);
    }

    //PresignedUrl 생성 - 매장
    public CreateS3PresignedUrlResponse createPresignedUrlStore(AuthUser authUser, CreateS3PresignedUrlRequest request) {
        Store store = storeService.getStore(request.getId());

        storeService.idMismatchCheck(authUser, store);

        UUID uuid = UUID.randomUUID();

        String fileExtension = request.getFilename().substring(request.getFilename().lastIndexOf('.') + 1);

        String contentType = ImageExtension.from(fileExtension);

        String key = s3Service.getKey(request.getId(), S3BucketStatus.STORE, uuid);

        return s3Service.getCreateS3PresignedUrlResponse(key, contentType, uuid);
    }

    //PresignedUrl 생성 - 상품
    public CreateS3PresignedUrlResponse createPresignedUrlProduct(AuthUser authUser, CreateS3PresignedUrlRequest request) {
        Product product = productService.getProduct(request.getId());

        productService.idMismatchCheck(authUser, product);

        UUID uuid = UUID.randomUUID();

        String fileExtension = request.getFilename().substring(request.getFilename().lastIndexOf('.') + 1);

        String contentType = ImageExtension.from(fileExtension);

        String key = s3Service.getKey(request.getId(), S3BucketStatus.PRODUCT, uuid);

        return s3Service.getCreateS3PresignedUrlResponse(key, contentType, uuid);
    }

}
