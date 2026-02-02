package com.allforone.starvestop.common.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${payment.secret-key}") // 일반결제 MID 시크릿키
    private String secretKey;

    @Value("${payment.billing-secret-key}") // 자동결제 MID 시크릿키
    private String secretBillingKey;

    @Value("${payment.base-url}")
    private String baseUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoAuthUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoApiUrl;

    private HttpClient defaultHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3_000)
                .responseTimeout(Duration.ofSeconds(5))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS))
                );
    }

    private WebClient buildTossWebClient(WebClient.Builder builder, String secretKey) {
        String basicToken = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        return builder
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(defaultHttpClient()))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + basicToken)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient paymentWebClient(WebClient.Builder builder) {
        return buildTossWebClient(builder, secretKey);
    }

    @Bean
    public WebClient paymentBillingWebClient(WebClient.Builder builder) {
        return buildTossWebClient(builder, secretBillingKey);
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
