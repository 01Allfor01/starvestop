package com.allforone.starvestop.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class WebClientConfig {

    @Value("${spring.payment.secret-key}")
    private String secretKey;

    @Value("${spring.payment.base-url}")
    private String baseUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoAuthUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoApiUrl;

    @Bean
    public WebClient paymentWebClient(WebClient.Builder builder) {
        String basicToken = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        return builder
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicToken)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient kakaoAuthWebClient(WebClient.Builder builder) {
        return builder.baseUrl(kakaoAuthUrl).build();
    }

    @Bean
    public WebClient kakaoApiWebClient(WebClient.Builder builder) {
        return builder.baseUrl(kakaoApiUrl).build();
    }
}