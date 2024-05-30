package com.example.classhubproject.service.user;

import com.example.classhubproject.data.user.UserResponseDTO;
import com.example.classhubproject.mapper.user.UserMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public UserService(Environment env) {
        this.env = env;
    }

//    public Map<Integer, UserResponseDTO> socialLogin(String code, String registrationId) {
//        String accessToken = getAccessToken(code, registrationId);
//        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
//
//        String snsId = userResourceNode.get("id").asText();
//        String email = userResourceNode.get("email").asText();
//        String name = userResourceNode.get("name").asText();
//        String nickname = userResourceNode.get("name").asText();
//        String picture = userResourceNode.get("picture").asText();
//
//        int check = userMapper.checkDuplicateBySnsId(snsId);
//        if (check != 1) {
//            UserResponseDTO userDTO = new UserResponseDTO(snsId, accessToken, name, nickname, email, picture);
//            check = userMapper.joinByGoogle(userDTO);
//        }
//
//        Integer userId = userMapper.selectUserIDBySnsId(snsId);
//        UserResponseDTO userResponseDTO = new UserResponseDTO(userId, snsId, accessToken, name, nickname, email, picture);
//
//        Map<Integer, UserResponseDTO> user = new HashMap<>();
//
//        if (check == 1) {
//            request.getSession().setAttribute("userId", userId);
//            user.put(1, userResponseDTO);
//            return user;
//        } else {
//            user.put(2, new UserResponseDTO());
//            return user;
//        }
//    }
//
//    private String getAccessToken(String authorizationCode, String registrationId) {
//        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
//        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
//        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
//        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");
//
//        // 환경 변수 값 로그 출력
//        System.out.println("Client ID: " + clientId);
//        System.out.println("Client Secret: " + clientSecret);
//        System.out.println("Redirect URI: " + redirectUri);
//        System.out.println("Token URI: " + tokenUri);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("code", authorizationCode);
//        params.add("client_id", clientId);
//        params.add("client_secret", clientSecret);
//        params.add("redirect_uri", redirectUri);
//        params.add("grant_type", "authorization_code");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
//
//        try {
//            ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
//            JsonNode accessTokenNode = responseNode.getBody();
//            return accessTokenNode.get("access_token").asText();
//        } catch (HttpClientErrorException e) {
//            System.err.println("HTTP Status Code: " + e.getStatusCode());
//            System.err.println("Response Body: " + e.getResponseBodyAsString());
//            throw new RuntimeException("Failed to get access token", e);
//        }
//    }
//
//    private JsonNode getUserResource(String accessToken, String registrationId) {
//        String resourceUri = env.getProperty("oauth2." + registrationId + ".resource-uri");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        HttpEntity entity = new HttpEntity(headers);
//        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
//    }

    public Integer join(UserResponseDTO userDTO) {
        int cnt = 0;
        int check = userMapper.checkDuplicateBySnsId(userDTO.getSnsId());
        if (check < 1) {
            cnt = userMapper.join(userDTO);
        }
        return cnt;
    }

    public UserResponseDTO selectUserBySnsId(String snsId) {
        return userMapper.selectUserBySnsId(snsId);
    }

    public void updateUserInfo(UserResponseDTO user) {
        userMapper.updateUserInfo(user);
    }

    public void updateUserImage(String snsId, MultipartFile file) {
        try {
            if (file != null) {
                // 파일 저장
                // ubuntu에 이미지 저장
                String originalFilename = file.getOriginalFilename();
                String newFileName = generateUniqueFileName(originalFilename);
                String folder = "/home/ubuntu/contents/images";
                file.transferTo(new File(folder + "/" + newFileName));

                //db에 관련 정보 저장
                userMapper.updateUserImage(snsId, "https://devproject.store" + folder + "/" + newFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        //Random 객체 생성
        Random random = new Random();
        // 0이상 100 미만의 랜덤한 정수 반환
        String randomNumber = Integer.toString(random.nextInt(Integer.MAX_VALUE));
        String timeStamp = dateFormat.format(new Date());
        return timeStamp + randomNumber + originalFileName;
    }
}
