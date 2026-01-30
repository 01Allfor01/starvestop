package com.allforone.starvestop.domain.auth.service;

import com.allforone.starvestop.common.dto.KakaoToken;
import com.allforone.starvestop.common.utils.JwtUtil;
import com.allforone.starvestop.common.utils.PasswordEncoder;
import com.allforone.starvestop.domain.auth.dto.response.SignInResponse;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthKakaoService {

    private final PasswordEncoder passwordEncoder;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect;

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final WebClient kakaoAuthWebClient;
    private final WebClient kakaoApiWebClient;

    public String loginKakao() {
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirect + "&response_type=code";
    }

    @Transactional
    public SignInResponse kakaoLogin(String code) throws JsonProcessingException {

        KakaoToken kakaoToken = kakaoAuthWebClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirect)
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoToken.class)
                .block();

        String userInfo = kakaoApiWebClient.post()
                .uri("/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoToken.getAccess_token())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(userInfo);

        Long providerId = json.get("id").asLong();

        User foundUser = userService.getKakaoUserOrElseSignUp(providerId);

        if (foundUser == null) {
            JsonNode accountJson = json.get("kakao_account");
            String email = UUID.randomUUID() + "@kakao.com";

            String nickname = accountJson.get("profile").get("nickname").asText();
            String password = passwordEncoder.encode(UUID.randomUUID().toString());
            User savedUser = userService.saveKakao(providerId, email, password, nickname, nickname);
            String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getEmail(), savedUser.getId(), savedUser.getRole());
            return new SignInResponse(token);
        }

        String token = jwtUtil.generateToken(foundUser.getUsername(), foundUser.getEmail(), foundUser.getId(), foundUser.getRole());
        return new SignInResponse(token);
    }
}
