package com.example.classhubproject.data.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 Request DTO")
public class CommentRequestDTO {

    @Schema(description = "회원 ID")
    private Integer userId;

    @Schema(description = "댓글 본문")
    private String text;

    @Schema(description = "게시물 ID")
    private Integer communityId;
}
