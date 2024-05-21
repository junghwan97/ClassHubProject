package com.example.classhubproject.data.community;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이미지 저장 Request DTO")
public class CommunityImageUploadRequestDTO {
    @Schema(description = "게시물 ID")
    private Integer communityId;

    @Schema(description = "이미지 ID")
    private Integer imageId;

    public CommunityImageUploadRequestDTO(Integer communityId, Integer imageId) {
        this.communityId = communityId;
        this.imageId = imageId;
    }
}
