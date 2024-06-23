package com.example.classhubproject.data.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "커뮤니티 Response DTO")
public class CommunityResponseDTO {

    @Schema(description = "회원 ID")
    private Integer userId;

    @Schema(description = "작성자")
    private String nickname;

    @Schema(description = "작성자 권한")
    private String role;

    @Schema(description = "작성자 프로필 이미지")
    private String profilePicture;

    @Schema(description = "게시물 ID")
    private Integer communityId;

    @Schema(description = "게시판 타입 ID", allowableValues = {"1", "2", "3"})
    private Character communityType;

    @Schema(description = "게시물 제목")
    private String title;

    @Schema(description = "게시물 본문")
    private String text;

    @Schema(description = "게시물 등록일자", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regDate;

    @Schema(description = "게시물 수정일자", format = "date-time")
    private Date editDate;

    @Schema(description = "게시물 좋아요 수")
    private Integer favoriteCount;

    @Schema(description = "게시물 댓글 수")
    private Integer commentCount;

    @Schema(description = "게시물 이미지 ID")
    private List<Integer> imageIds;

    @Schema(description = "게시물 이미지명")
    private List<String> image;

    @Schema(description = "게시물 좋아요 누른 회원 리스트")
    private List<Integer> likeUsers;
}
