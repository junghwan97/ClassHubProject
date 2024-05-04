package com.example.classhubproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LMS API")
                        .description("LMS 프로젝트 샘플입니다.")
                        .version("1.0"));
    }

    @Bean
    public GroupedOpenApi group1() {
        return GroupedOpenApi.builder()
                .group("샘플 기능")
                .pathsToMatch("/sample/*")
                .packagesToScan("com.example.classhubproject.controller.sample")
                .build();
    }
}