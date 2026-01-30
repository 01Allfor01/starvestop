package com.allforone.starvestop.domain.s3.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadImageRequest {
    @NotNull(message = "아이디를 입력해주세요")
    private Long id;
    @NotBlank(message = "식별자를 입력해주세요")
    private String imageUuid;
}
