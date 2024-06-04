package com.example.classhubproject.service.oauth2;

import com.example.classhubproject.data.oauth2.*;
import com.example.classhubproject.data.user.UserResponseDTO;
import com.example.classhubproject.mapper.user.UserMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;

    public CustomOAuth2UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserResponseDTO existUser = userMapper.selectUserByUsername(username);

        String RoleUser = "ROLE_USER";

        if(existUser == null) {
            UserResponseDTO user = new UserResponseDTO(username, oAuth2Response.getEmail(), oAuth2Response.getName(), RoleUser);
            userMapper.insertGoogle(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
            UserResponseDTO user = new UserResponseDTO(existUser.getUserId(), existUser.getUserName(), oAuth2Response.getEmail(), oAuth2Response.getName(), existUser.getRole());

            userMapper.updateGoogle(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existUser.getUserName());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existUser.getUserName());

            return new CustomOAuth2User(userDTO);
    }

}