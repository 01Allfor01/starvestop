package com.allforone.starvestop.common.utils;

import com.allforone.starvestop.common.dto.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtUtil.generateToken(principal.getUsername(), principal.getEmail(), principal.getUserId(), principal.getRole());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = Map.of(
                "accessToken", accessToken
        );

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

}
