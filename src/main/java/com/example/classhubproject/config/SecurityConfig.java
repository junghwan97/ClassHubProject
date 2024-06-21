package com.example.classhubproject.config;

import com.example.classhubproject.jwt.CustomLogoutSuccessHandler;
import com.example.classhubproject.jwt.CustomSuccessHandler;
import com.example.classhubproject.jwt.JWTFilter;
import com.example.classhubproject.jwt.JWTUtil;
import com.example.classhubproject.service.oauth2.CustomOAuth2UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/**",
            "/docs/**",
            "/v3/**",
            "/community/questions/**",
            "/community/studies/**",
            "/community/question/**",
            "/community/study/**",
            "/community/mainpage",
            "/lecture/selectMaterial/**",
            "/lecture/selectclassDetail/**",
            "/lecture/selectById/**",
            "/lecture/selectByCategory/**",
            "/lecture/selectAll/**",
            "/lecture/**",
            "/comment/list",
            "/home/ubuntu/**"
    };


    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil, CustomLogoutSuccessHandler customLogoutSuccessHandler) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        http
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//                        CorsConfiguration configuration = new CorsConfiguration();
//
////                        configuration.setAllowedOrigins(Collections.singletonList("https://devproject.store"));
//                        configuration.setAllowedOrigins(List.of("https://devproject.store", "http://localhost:3000"));
////                        configuration.setAllowedMethods(Collections.singletonList("*"));
////                        configuration.setAllowedMethods(List.of("*"));
//                        configuration.setAllowedMethods(List.of("POST, GET, PUT, OPTIONS, DELETE, PATCH"));
//                        configuration.setAllowCredentials(true);
////                        configuration.setAllowedHeaders(Collections.singletonList("*"));
//                        configuration.setAllowedHeaders(List.of("*"));
//                        configuration.setMaxAge(3600L);
//
////                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
//                        configuration.setExposedHeaders(List.of("Set-Cookie"));
////                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//                        configuration.setExposedHeaders(List.of("Authorization"));
//
//                        return configuration;
//                    }
//                }));

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //JWTFilter 추가
        http
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);


        //oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

        //경로별 인가 작업
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                )
        ;

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //로그아웃 설정
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutSuccessHandler)
                        .deleteCookies("Authorization")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            // 쿠키 삭제
            String cookieName = "Authorization";
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setPath("/");
//            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        };
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://devproject.store", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("POST, GET, PUT, OPTIONS, DELETE, PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
        configuration.setMaxAge(3600L);

        return request -> configuration;
    }
}

