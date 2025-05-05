package com.kkiri_trip.back.global.config.web;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Trebuddy API")
                        .description("여행 매칭 플랫폼 Trebuddy API 문서입니다.")
                        .version("v1.0.0"));
    }
}

