package com.allforone.starvestop.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "비밀번호는 8자 이상, 영문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다"
    )
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;
    @NotBlank(message = "역할을 정해주세요")
    private String role;

}
