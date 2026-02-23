package com.allforone.starvestop.domain.s3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이미지 업로드 완료 처리 요청")
public class UploadImageRequest {
    @Schema(description = "대상 리소스 ID (userId/storeId/productId)", example = "1")
    @NotNull(message = "아이디를 입력해주세요")
    private Long id;
    @Schema(description = "이미지 UUID", example = "3f7b7d1a-8c2a-4b8c-9f9a-1c2d3e4f5a6b")
    @NotBlank(message = "식별자를 입력해주세요")
    private String imageUuid;
}
