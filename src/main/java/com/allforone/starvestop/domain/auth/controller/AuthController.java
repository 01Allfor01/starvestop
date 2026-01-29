package com.allforone.starvestop.domain.auth.controller;

import com.allforone.starvestop.common.dto.CommonResponse;
import com.allforone.starvestop.common.dto.KakaoToken;
import com.allforone.starvestop.domain.auth.dto.request.SignInRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpOwnerRequest;
import com.allforone.starvestop.domain.auth.dto.request.SignUpRequest;
import com.allforone.starvestop.domain.auth.dto.response.SignInResponse;
import com.allforone.starvestop.domain.auth.dto.response.SignUpResponse;
import com.allforone.starvestop.domain.auth.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.Map;

import static com.allforone.starvestop.common.enums.SuccessMessage.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ObjectMapper objectMapper;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final AuthService authService;

    private final WebClient kakaoAuthWebClient;

    private final WebClient kakaoApiWebClient;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = authService.signUp(request);

        CommonResponse<SignUpResponse> result = CommonResponse.success(SIGN_UP_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<CommonResponse<SignInResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        SignInResponse response = authService.signIn(request);

        CommonResponse<SignInResponse> result = CommonResponse.success(SIGN_IN_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    // 회원 가입 - 판매자
    @PostMapping("/signup/owner")
    public ResponseEntity<CommonResponse<SignUpResponse>> signUpOwner(@Valid @RequestBody SignUpOwnerRequest request) {
        SignUpResponse response = authService.signUpOwner(request);

        CommonResponse<SignUpResponse> result = CommonResponse.success(SIGN_UP_OWNER_SUCCESS, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // 로그인 - 판매자
    @PostMapping("/signin/owner")
    public ResponseEntity<CommonResponse<SignInResponse>> signInOwner(@Valid @RequestBody SignInRequest request) {
        SignInResponse response = authService.signInOwner(request);

        CommonResponse<SignInResponse> result = CommonResponse.success(SIGN_IN_SUCCESS, response);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<String> loginKakao(HttpServletResponse response) {
        String str = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirect + "&response_type=code";
        return ResponseEntity.status(HttpStatus.OK).body(str);
    }

    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) throws JsonProcessingException {

        KakaoToken kakaoToken = kakaoAuthWebClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirect)
                        .with("code", code))
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new RuntimeException("Kakao token error: " + body))))
                .bodyToMono(KakaoToken.class)
                .block();

        log.info("code={}", code);
        log.info("redirect_uri used for token={}", redirect);

        String userInfo = kakaoApiWebClient.post()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoToken.getAccess_token())
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new RuntimeException())))
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(userInfo);
    }
}
