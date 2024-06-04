package com.example.classhubproject.service;

import com.example.classhubproject.data.CustomOAuth2User;
import com.example.classhubproject.data.GoogleResponse;
import com.example.classhubproject.data.OAuth2Response;
import com.example.classhubproject.data.user.UserDTO;
import com.example.classhubproject.data.user.UserRequestDTO;
import com.example.classhubproject.data.user.UserResponseDTO;
import com.example.classhubproject.mapper.user.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String snsId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        UserResponseDTO existData = userMapper.selectUserByUsername(snsId);

        if (existData == null) {
            UserRequestDTO user = UserRequestDTO.builder()
                    .snsId(snsId)
                    .name(oAuth2Response.getName())
                    .nickname(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();
            userMapper.joinByGoogle(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(snsId);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {
//            existData.builder()
//                    .email(oAuth2Response.getEmail())
//                    .name(oAuth2Response.getName())
//                    .nickname(oAuth2Response.getName())
//                    .build();

            UserResponseDTO updatedData = UserResponseDTO.builder()
                    .snsId(existData.getSnsId()) // 기존 데이터 복사
                    .name(oAuth2Response.getName()) // 업데이트할 필드
                    .nickname(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role(existData.getRole()) // 기존 데이터 복사
                    .build();

            userMapper.updateGoogleAccount(updatedData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getName());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());
            return new CustomOAuth2User(userDTO);

        }
    }
}