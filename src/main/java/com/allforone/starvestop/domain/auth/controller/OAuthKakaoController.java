package com.allforone.starvestop.domain.auth.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.domain.auth.dto.response.SignInResponse;
import com.allforone.starvestop.domain.auth.service.OAuthKakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.allforone.starvestop.common.enums.SuccessMessage.SIGN_IN_SUCCESS;
import static com.allforone.starvestop.common.enums.SuccessMessage.URL_RETURN_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth/kakao")
public class OAuthKakaoController {

    private final OAuthKakaoService oAuthKakaoService;

    @GetMapping
    public ResponseEntity<CommonResponse<String>> loginKakao() {
        String url = oAuthKakaoService.loginKakao();
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success(URL_RETURN_SUCCESS, url));
    }

    @GetMapping("/callback")
    public ResponseEntity<CommonResponse<SignInResponse>> kakaoCallback(@RequestParam String code) throws JsonProcessingException {

        SignInResponse response = oAuthKakaoService.kakaoLogin(code);

        return ResponseEntity.ok(CommonResponse.success(SIGN_IN_SUCCESS, response));
    }
}
