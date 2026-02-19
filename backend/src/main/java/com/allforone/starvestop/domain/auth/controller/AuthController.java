package com.allforone.starvestop.domain.auth.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.auth.dto.request.SignInRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpAdminRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpOwnerRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpRequest;
import com.allforone.starvestop.domain.auth.dto.response.SignInResponse;
import com.allforone.starvestop.domain.auth.dto.response.SignUpResponse;
import com.allforone.starvestop.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Tag(name = "Auth", description = "회원가입/로그인")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원 가입
    @Operation(summary = "회원 가입(사용자)")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUp(
            @Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);

        CommonResponse<SignUpResponse> result = CommonResponse.success(SIGN_UP_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인
    @Operation(summary = "로그인(사용자)")
    @PostMapping("/signin")
    public ResponseEntity<CommonResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        SignInResponse response = authService.signIn(request);

        CommonResponse<SignInResponse> result = CommonResponse.success(SIGN_IN_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    // 회원 가입 - 판매자
    @Operation(summary = "회원 가입(판매자)")
    @PostMapping("/signup/owner")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUpOwner(@Valid @RequestBody SignUpOwnerRequest request) {
        SignUpResponse response = authService.signUpOwner(request);

        CommonResponse<SignUpResponse> result = CommonResponse.success(SIGN_UP_OWNER_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인 - 판매자
    @Operation(summary = "로그인(판매자)")
    @PostMapping("/signin/owner")
    public ResponseEntity<CommonResponse<SignInResponse>> signInOwner(@Valid @RequestBody SignInRequest request) {
        SignInResponse response = authService.signInOwner(request);

        CommonResponse<SignInResponse> result = CommonResponse.success(SIGN_IN_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    // 회원가입 - 관리자
    @Operation(summary = "회원 가입(관리자)")
    @PostMapping("/signup/admin")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUpAdmin(@Valid @RequestBody SignUpAdminRequest request) {
        SignUpResponse response = authService.signUpAdmin(request);

        CommonResponse<SignUpResponse> result = CommonResponse.success(SIGN_UP_ADMIN_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인 - 관리자
    @Operation(summary = "로그인(관리자)")
    @PostMapping("/signin/admin")
    public ResponseEntity<CommonResponse<SignInResponse>> signInAdmin(@Valid @RequestBody SignInRequest request) {
        SignInResponse response = authService.signInAdmin(request);

        CommonResponse<SignInResponse> result = CommonResponse.success(SIGN_IN_SUCCESS, response);

        return ResponseEntity.ok(result);
    }
}
