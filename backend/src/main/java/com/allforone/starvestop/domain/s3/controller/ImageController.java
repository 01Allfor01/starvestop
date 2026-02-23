package com.allforone.starvestop.domain.s3.controller;

import com.allforone.starvestop.common.config.OpenApiConfig;
import com.allforone.starvestop.common.docs.ApiRoleLabels;
import com.allforone.starvestop.common.dto.AuthUser;
import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.s3.dto.request.UploadImageRequest;
import com.allforone.starvestop.domain.s3.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.IMAGE_UPLOAD_SUCCESS;

@Tag(name = "Images", description = "이미지 업로드 완료 처리")
@SecurityRequirement(name = OpenApiConfig.BEARER)
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    //사용자 이미지 업로드 완료
    @Operation(summary = "사용자 이미지 업로드 완료" + ApiRoleLabels.USER)
    @PostMapping("/users/images")
    public ResponseEntity<CommonResponse<Void>> uploadUserImage(@AuthenticationPrincipal AuthUser authUser,
                                                                @RequestBody UploadImageRequest request) {
        imageService.uploadUserImage(authUser.getUserId(), request);

        CommonResponse<Void> response = CommonResponse.successNoData(IMAGE_UPLOAD_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //매장 이미지 업로드 완료
    @Operation(summary = "매장 이미지 업로드 완료" + ApiRoleLabels.OWNER_ADMIN)
    @PostMapping("/stores/images")
    public ResponseEntity<CommonResponse<Void>> uploadStoreImage(@AuthenticationPrincipal AuthUser authUser,
                                                                 @RequestBody UploadImageRequest request) {
        imageService.uploadStoreImage(authUser, request);

        CommonResponse<Void> response = CommonResponse.successNoData(IMAGE_UPLOAD_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //상품 이미지 업로드 완료
    @Operation(summary = "상품 이미지 업로드 완료" + ApiRoleLabels.OWNER)
    @PostMapping("/products/images")
    public ResponseEntity<CommonResponse<Void>> uploadProductImage(@AuthenticationPrincipal AuthUser authUser,
                                                                   @RequestBody UploadImageRequest request) {
        imageService.uploadProductImage(authUser, request);
        CommonResponse<Void> response = CommonResponse.successNoData(IMAGE_UPLOAD_SUCCESS);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
