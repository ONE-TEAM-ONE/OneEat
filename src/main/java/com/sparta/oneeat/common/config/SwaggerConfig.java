package com.sparta.oneeat.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(

                // 서비스 이름
                title = "OneEat",

                // 서비스 설명
                description = "Chapter 1. AI 검증 비즈니스 프로젝트(1조-원팀)",

                // 서비스 버전
                version = "1.0.0"
        )
)

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        // 인증 요청 방식에 HEADER 추가
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");

        // bearerAuth 이름으로 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()

                // SecurityRequirement에 정의한 bearerAuth, 위에서 정의한 securityScheme 추가
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))

                // 보안 규칙 추가
                .security(Arrays.asList(securityRequirement));
    }
}