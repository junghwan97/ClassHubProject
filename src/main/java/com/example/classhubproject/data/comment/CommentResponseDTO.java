package com.example.classhubproject.data.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 Response DTO")
public class CommentResponseDTO {

    @Schema(description = "댓글 고유 번호")
    private Integer commentId;

    @Schema(description = "회원 ID")
    private Integer userId;

    @Schema(description = "게시물 고유 번호")
    private Integer communityId;

    @Schema(description = "댓글 본문")
    private String text;

    @Schema(description = "댓글 좋아요 수")
    private String favoriteCount;

    @Schema(description = "게시물 등록일자", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regDate;

    @Schema(description = "게시물 수정일자", format = "date-time")
    private Date editDate;
}
