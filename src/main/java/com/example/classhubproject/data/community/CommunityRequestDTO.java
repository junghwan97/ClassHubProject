package com.example.classhubproject.data.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "커뮤니티 Request DTO")
public class CommunityRequestDTO {

    @Schema(description = "회원 ID")
    private Integer userId;

    @Schema(description = "게시물 ID")
    private Integer communityId;

    @Schema(description = "게시판 타입 ID")
    private Character communityType;

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 본문")
    private String text;
}
