package com.allforone.starvestop.domain.s3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "Presigned URL 생성 요청")
public class CreateS3PresignedUrlRequest {
    @Schema(description = "대상 리소스 ID (userId/storeId/productId)", example = "1")
    @NotNull(message = "아이디를 입력해주세요")
    private Long id;
    @Schema(description = "원본 파일명", example = "profile.png")
    @NotBlank(message = "파일 이름을 입력해주세요")
    private String filename;
}