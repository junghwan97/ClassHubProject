package com.example.classhubproject.jwt;

import com.example.classhubproject.data.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    public CustomSuccessHandler(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 60 * 60 * 1000L -> 1시간 * 7(7시간) * 24(일주일)
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 1000L * 7 * 24);

        response.addCookie(createCookie("Authorization", token));
        System.out.println(token);

//        System.out.println(((CustomOAuth2User) authentication.getPrincipal()).getUserDTO().getUsername());
        System.out.println("username : " + username + "===========================================");
        System.out.println("role : " + role + "===========================================");

//        response.sendRedirect("https://devproject.store/");
        response.sendRedirect("http://local.devproject.store:3000");
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        cookie.setDomain("devproject.store");
//        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setHttpOnly(true);

        return cookie;
    }
}