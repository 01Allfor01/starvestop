package com.allforone.starvestop.domain.auth.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.auth.dto.request.SignUpRequest;
import com.allforone.starvestop.domain.auth.dto.response.SignUpResponse;
import com.allforone.starvestop.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.SIGN_UP_SUCCESS;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);

        CommonResponse<SignUpResponse> result = CommonResponse.success(SIGN_UP_SUCCESS, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
