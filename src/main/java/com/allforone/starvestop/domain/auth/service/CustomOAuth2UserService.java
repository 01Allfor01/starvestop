package com.allforone.starvestop.domain.auth.service;

import com.allforone.starvestop.common.dto.CustomOAuth2User;
import com.allforone.starvestop.common.utils.JwtUtil;
import com.allforone.starvestop.domain.user.entity.User;
import com.allforone.starvestop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!"kakao".equals(registrationId)) {
            throw new OAuth2AuthenticationException("잘못된 접근입니다");
        }

        Object idObject = oAuth2User.getAttributes().get("sub");
        String providerId = String.valueOf(idObject);

        User user = userRepository.findByProviderIdAndIsDeletedIsFalse(providerId);

        if (user != null) {
            return new CustomOAuth2User(user.getUsername(), user.getEmail(), user.getId(), user.getRole(), oAuth2User.getAttributes());
        }

            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            String email = UUID.randomUUID() + "@kakao.com";

            Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;
            String nickname = profile != null ? (String)profile.get("nickname") : "KakaoUser";


        User newUser = User.createKakao(email, UUID.randomUUID().toString(),nickname, nickname, providerId);
        User savedUser = userRepository.save(newUser);

        return new CustomOAuth2User(savedUser.getUsername(), savedUser.getEmail(), savedUser.getId(), savedUser.getRole(), oAuth2User.getAttributes());
    }

}
