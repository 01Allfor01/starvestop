package com.allforone.starvestop.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequest {
    @Email(message = "이메일 형식이 아닙니다")
    @NotBlank(message = "이메일을 입력해주세요")
    @Size(max = 100, message = "이메일은 100자 이하로 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
}
