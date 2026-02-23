package com.allforone.starvestop.domain.owner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "판매자 정보 수정 요청")
@Getter
@NoArgsConstructor
public class UpdateOwnerRequest {
    @Schema(description = "비밀번호(8자 이상, 영문/숫자/특수문자 포함)", example = "Test!2345")
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "비밀번호는 8자 이상, 영문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다"
    )
    private String password;
}
