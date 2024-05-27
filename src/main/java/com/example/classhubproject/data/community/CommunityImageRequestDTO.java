package com.example.classhubproject.data.community;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "커뮤니티 이미지 DTO")
public class CommunityImageRequestDTO {

    @Schema(description = "이미지 ID")
    private Integer communityImageId;

    @Schema(description = "이미지 경로")
    private String imagePath;
}


