package com.allforone.starvestop.common.config;

import com.allforone.starvestop.common.exception.JwtAccessDeniedHandler;
import com.allforone.starvestop.common.exception.JwtAuthenticationEntryPoint;
import com.allforone.starvestop.common.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers(
                                "/payment.html",
                                "/success.html",
                                "/fail.html",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/billing.html",
                                "/billing-success.html"
                        ).permitAll()

                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/login/**", "/oauth2/**").permitAll()
                        .requestMatchers("/ws-stomp/**").permitAll()
                        .requestMatchers("/notifications/**").permitAll()
                        .requestMatchers("/pushtest.html").permitAll()
                        .requestMatchers("firebase-messaging-sw.js").permitAll()
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.GET, "/payments/success").permitAll()
                        .requestMatchers(HttpMethod.GET, "/payments/fail").permitAll()

                        .requestMatchers(HttpMethod.PATCH, "/users").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/users").hasRole("USER")

                        .requestMatchers(HttpMethod.PATCH, "/owners").hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/owners").hasRole("OWNER")

                        .requestMatchers(HttpMethod.POST, "/stores/*/chat-rooms").hasRole("USER")

                        .requestMatchers(HttpMethod.POST, "/products/**").hasAnyRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/products/**").hasAnyRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/stores/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/stores/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/stores/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/subscriptions/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/subscriptions/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/subscriptions/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/coupons/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/coupons/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api-logs/**").hasAnyRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}