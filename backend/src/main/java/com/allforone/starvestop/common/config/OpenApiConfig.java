package com.allforone.starvestop.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("""
                        JWT Bearer 토큰을 입력하세요.
                        
                        예시:
                        eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
                        
                        'Bearer '는 자동으로 붙습니다.
                        """);

        return new OpenAPI()
                .info(new Info()
                        .title("StarveStop API")
                        .version("v1.0")
                        .description("StarveStop Backend API Documentation")
                )
                .components(new Components()
                        .addSecuritySchemes(BEARER, securityScheme)
                )
                .addSecurityItem(new SecurityRequirement().addList(BEARER));
    }
}
