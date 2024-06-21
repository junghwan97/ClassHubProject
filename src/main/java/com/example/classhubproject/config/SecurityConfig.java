package com.example.classhubproject.config;

import com.example.classhubproject.jwt.CustomSuccessHandler;
import com.example.classhubproject.jwt.JWTFilter;
import com.example.classhubproject.jwt.JWTUtil;
import com.example.classhubproject.service.oauth2.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

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


    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
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

        return http.build();
    }
}

