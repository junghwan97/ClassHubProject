package com.example.classhubproject.data.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원 Response DTO")
public class UserRequestDTO {

    @Schema(description = "회원 ID")
    private String snsId;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 닉네임")
    private String nickname;

    @Schema(description = "회원 email")
    private String email;

    @Schema(description = "회원 프로필 이미지")
    private String profilePicture;

    @Schema(description = "회원 등록일자", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regDate;

    @Schema(description = "회원 탈퇴일자", format = "date-time")
    private Date exitDate;

    @Schema(description = "회원 권한", defaultValue = "1", allowableValues = {"1", "2"})
    private String role;
}
