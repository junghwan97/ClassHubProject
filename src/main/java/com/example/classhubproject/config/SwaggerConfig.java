package com.example.classhubproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Configuration
public class SwaggerConfig {

    Server server = new Server().url("https://api.devproject.store");



    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("LMS API")
                .description("LMS 프로젝트 샘플입니다.");

        SecurityScheme securityScheme = new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")
                .scheme("bearer");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("JWT");

        return new OpenAPI()
                .servers(List.of(server))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes("JWT", securityScheme));
    }



    // @Bean
    // public OpenAPI openAPI() {
    //     return new OpenAPI()
    //             .servers(List.of(server))
    //             .info(new Info()
    //                     .title("LMS API")
    //                     .description("LMS 프로젝트 샘플입니다.")
    //                     .version("1.0"));
    // }

  
    @Bean
    public GroupedOpenApi group1() {
        return GroupedOpenApi.builder()
                .group("샘플 기능")
                .pathsToMatch("/sample/*")
                .packagesToScan("com.example.classhubproject.controller.sample")
                .build();
    }

    @Bean
    public GroupedOpenApi cart() {
        return GroupedOpenApi.builder()
                .group("장바구니 기능")
                .pathsToMatch("/cart/*")
                .packagesToScan("com.example.classhubproject.controller.cart")
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
                .pathsToMatch("/lecture/*")
                .packagesToScan("com.example.classhubproject.controller.lecture")
                .build();
    }

    @Bean
    public GroupedOpenApi order() {
        return GroupedOpenApi.builder()
                .group("주문 관련")
                .pathsToMatch("/order/*")
                .packagesToScan("com.example.classhubproject.controller.order")
                .build();
    }

    @Bean
    public GroupedOpenApi user() {
        return GroupedOpenApi.builder()
                .group("회원 기능")
                .pathsToMatch("/user/*")
                .packagesToScan("com.example.classhubproject.controller.user")
                .build();
    }
  
    @Bean
    public GroupedOpenApi payment () {
        return GroupedOpenApi.builder()
                .group("결제 관련")
                .pathsToMatch("/payment/*")
                .packagesToScan("com.example.classhubproject.controller.payment")
                .build();
    }

}

