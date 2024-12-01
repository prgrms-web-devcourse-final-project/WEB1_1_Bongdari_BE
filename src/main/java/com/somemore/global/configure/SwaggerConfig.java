package com.somemore.global.configure;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT 토큰을 이용한 인증");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("AccessToken");

        return new OpenAPI()
                .info(new Info()
                        .title("Somemore API")
                        .version("1.0")
                        .description("Somemore swagger-ui 화면입니다.")
                )
                .components(new Components()
                        .addSecuritySchemes("AccessToken", securityScheme)
                )
                .addSecurityItem(securityRequirement);
    }
}
