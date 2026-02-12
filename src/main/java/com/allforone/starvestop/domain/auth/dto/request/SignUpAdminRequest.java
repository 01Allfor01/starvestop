package com.allforone.starvestop.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpAdminRequest {
    @Email(message = "이메일 형식이 아닙니다")
    @Size(max = 100, message = "이메일은 100자 이하로 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    public String email;

    @NotBlank(message = "패스워드를 입력해주세요")
    @Size(max = 100, message = "패스워드는 100자 이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "비밀번호는 8자 이상, 영문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다")
    private String password;

    @NotBlank(message = "관리자 키를 입력해주세요")
    private String adminKey;
}
