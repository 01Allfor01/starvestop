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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                        .requestMatchers("/ws-stomp/**").permitAll()
                        .requestMatchers("/pushtest.html").permitAll()
                        .requestMatchers("firebase-messaging-sw.js").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        .requestMatchers(HttpMethod.GET, "/payments/success").permitAll()
                        .requestMatchers(HttpMethod.GET, "/payments/fail").permitAll()

                        //USER
                        .requestMatchers(HttpMethod.POST, "/coupons/{couponId}/user-coupons").hasAnyRole("USER")
                        .requestMatchers("/users/**").hasRole("USER")
                        .requestMatchers("/payments/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/stores/*/chat-rooms").hasRole("USER")
                        .requestMatchers("/carts").hasRole("USER")
                        .requestMatchers("/user-coupons/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/coupons/*/user-coupons").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/coupons/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/billing/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/orders").hasRole("USER")

                        //OWNER
                        .requestMatchers("/owners/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/products/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/products/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.POST, "/stores/**").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PATCH, "/stores/**").hasRole("OWNER")
                        .requestMatchers("stores/my").hasRole("OWNER")

                        //ADMIN
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/coupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/coupons/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/coupons/**").hasRole("ADMIN")
                        .requestMatchers("/api-logs/**").hasRole("ADMIN")
                        .requestMatchers("/payment-logs/**").hasRole("ADMIN")
                        .requestMatchers("/settlements/**").hasRole("ADMIN")
                        .requestMatchers("/settlement-logs/**").hasRole("ADMIN")

                        //USER & OWNER
                        .requestMatchers("/chat-rooms/**").hasAnyRole("USER", "OWNER")
                        .requestMatchers("/notifications/save/token").hasAnyRole("USER", "OWNER")

                        //OWNER & ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/stores/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/subscriptions/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/subscriptions/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/subscriptions/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/notifications/send/**").hasAnyRole("OWNER", "ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        configuration.setExposedHeaders(List.of("Authorization", "AccessToken"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}