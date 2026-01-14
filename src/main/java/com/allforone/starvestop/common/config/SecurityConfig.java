package com.allforone.starvestop.common.config;

import com.allforone.starvestop.common.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/products/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/products/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/products/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/stores/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/stores/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/stores/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/subscriptions/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/subscriptions/**").hasAnyRole("OWNER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/subscriptions/**").hasAnyRole("OWNER","ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler((req, res, e) ->
                                res.sendError(HttpServletResponse.SC_FORBIDDEN)
                        )
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}