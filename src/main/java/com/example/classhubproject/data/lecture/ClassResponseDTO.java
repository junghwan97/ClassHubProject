package com.example.classhubproject.data.lecture;

import com.example.classhubproject.data.category.CategoryResponseDTO;
import com.example.classhubproject.data.instructor.InstructorsResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;

@Getter
@NoArgsConstructor
@Schema(description = "강의 Response DTO")
public class ClassResponseDTO {

    @Schema(description = "강의 고유 번호")
    private int classId;

    @Schema(description = "강사 ID")
    private int instructorsId;

    @Schema(description = "카테고리 ID")
    private int categoryId;

    @Schema(description = "강의명")
    private String className;

    @Schema(description = "강의 전체 설명")
    private String description;

    @Schema(description = "강의 요약정보 설명")
    private String summary;

    @Schema(description = "강의 가격")
    private int price;

    @Schema(description = "강의 영상 미리보기 URL")
    private String thumnail;

    @Schema(description = "강의 총 영상 길이(재생시간)")
    private int totalVideoLength;

    @Schema(description = "강의 평점")
    private double reviewScore;

    @Schema(description = "강의 등록일", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regdate;

    @Schema(description = "수정일자")
    private Date editDate;

    @Schema(description = "카테고리 정보", implementation = CategoryResponseDTO.class)
    private CategoryResponseDTO categoryResponseDTO;

    @Schema(description = "강사 정보", implementation = CategoryResponseDTO.class)
    private InstructorsResponseDTO instructorsResponseDTO;

}
