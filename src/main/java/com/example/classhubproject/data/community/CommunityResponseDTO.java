package com.example.classhubproject.data.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Schema(description = "커뮤니티 Response DTO")
public class CommunityResponseDTO {

    @Schema(description = "회원 ID")
    private Integer userId;

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

    public CommunityResponseDTO(Integer userId, Integer communityId, Character communityType, String title, String text, Date regDate, Date editDate, Integer favoriteCount, Integer commentCount, List<Integer> imageIds, List<String> image, List<Integer> likeUsers) {
        this.userId = userId;
        this.communityId = communityId;
        this.communityType = communityType;
        this.title = title;
        this.text = text;
        this.regDate = regDate;
        this.editDate = editDate;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
        this.imageIds = imageIds;
        this.image = image;
        this.likeUsers = likeUsers;
    }
}
