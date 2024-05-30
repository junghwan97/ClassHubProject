package com.example.classhubproject.data.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "카테고리 DTO")
public class CategoryResponseDTO {

    @Schema(description = "카테고리 고유 번호")
    private int categoryId;

    @Schema(description = "카테고리명")
    private String categoryName;
}
