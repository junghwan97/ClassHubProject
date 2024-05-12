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

    @Bean
    public GroupedOpenApi community() {
        return GroupedOpenApi.builder()
                .group("게시판 기능")
                .pathsToMatch("/community/*")
                .packagesToScan("com.example.classhubproject.controller.community")
                .build();
    }

    @Bean
    public GroupedOpenApi comment() {
        return GroupedOpenApi.builder()
                .group("댓글 기능")
                .pathsToMatch("/comment/*")
                .packagesToScan("com.example.classhubproject.controller.comment")
                .build();
    }

    @Bean
    public GroupedOpenApi favorite() {
        return GroupedOpenApi.builder()
                .group("좋아요 기능")
                .pathsToMatch("/favorite/*")
                .packagesToScan("com.example.classhubproject.controller.favorite")
                .build();
    }

    @Bean
    public GroupedOpenApi lecture() {
        return GroupedOpenApi.builder()
                .group("강의 관련")
                .pathsToMatch("/order/*")
                .packagesToScan("com.example.classhubproject.controller.lecture")
                .build();
    }
}