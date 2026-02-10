package com.allforone.starvestop.domain.s3.controller;

import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.s3.dto.request.UploadImageRequest;
import com.allforone.starvestop.domain.s3.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.IMAGE_UPLOAD_SUCCESS;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    //사용자 이미지 업로드 완료
    @PostMapping("/users/images")
    public ResponseEntity<CommonResponse<Void>> uploadUserImage(@AuthenticationPrincipal AuthUser authUser,
                                                                @RequestBody UploadImageRequest request) {
        imageService.uploadUserImage(authUser.getUserId(), request);

        CommonResponse<Void> response = CommonResponse.successNoData(IMAGE_UPLOAD_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //매장 이미지 업로드 완료
    @PostMapping("/stores/images")
    public ResponseEntity<CommonResponse<Void>> uploadStoreImage(@AuthenticationPrincipal AuthUser authUser,
                                                                 @RequestBody UploadImageRequest request) {
        imageService.uploadStoreImage(authUser, request);

        CommonResponse<Void> response = CommonResponse.successNoData(IMAGE_UPLOAD_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 이미지 업로드 완료
    @PostMapping("/products/images")
    public ResponseEntity<CommonResponse<Void>> uploadProductImage(@AuthenticationPrincipal AuthUser authUser,
                                                                   @RequestBody UploadImageRequest request) {
        imageService.uploadProductImage(authUser, request);
        CommonResponse<Void> response = CommonResponse.successNoData(IMAGE_UPLOAD_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
