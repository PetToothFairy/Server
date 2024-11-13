package com.example.server.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("양치코치 API 문서")
                        .description("이빨요정 팀의 양치코치 서버 API 문서입니다.")
                        .version("v1.0"));
    }
}
