package com.example.classhubproject.data.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Getter
@NoArgsConstructor
@Schema(description = "회원 Response DTO")
public class UserResponseDTO {

    @Schema(description = "회원 식별 ID")
    private int userId;

    @Schema(description = "회원 ID")
    private String snsId;

    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 email")
    private String email;

    @Schema(description = "회원 프로필 이미지")
    private String profilePicture ;

    @Schema(description = "회원 소개글")
    private String introduce ;

    @Schema(description = "회원 등록일자", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regDate;

    @Schema(description = "회원 탈퇴일자", format = "date-time")
    private Date exitDate;

    @Schema(description = "회원 권한", defaultValue = "1", allowableValues = {"1", "2"})
    private String role;

    public UserResponseDTO(String snsId, String accessToken, String name, String nickname, String email, String profilePicture) {

        this.snsId = snsId;
        this.accessToken = accessToken;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profilePicture = profilePicture;
    }
    public UserResponseDTO(Integer userId, String snsId, String accessToken, String name, String nickname, String email, String profilePicture) {
        this.userId = userId;
        this.snsId = snsId;
        this.accessToken = accessToken;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.profilePicture = profilePicture;
    }
}
