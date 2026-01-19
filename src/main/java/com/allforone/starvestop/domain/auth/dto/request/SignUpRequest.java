package com.allforone.starvestop.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
    @Email(message = "이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 100자 이하로 입력해주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "패스워드를 입력해주세요")
    @Size(max = 100, message = "패스워드는 100자 이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
            message = "비밀번호는 8자 이상, 영문자, 숫자, 특수문자를 최소 1개씩 포함해야 합니다"
    )
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 15, message = "닉네임은 2자 이상 15자 이하로 입력해주세요")
    private String nickname;
    @NotBlank(message = "유저이름을 입력해주세요")
    @Size(min = 2, max = 30, message = "유저명은 2자 이상 30자 이하로 입력해주세요")
    private String username;
}
