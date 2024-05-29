package com.example.classhubproject.data.instructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Schema(description = "강사 Response DTO")
public class InstructorsResponseDTO {

    @Schema(description = "강사 ID")
    private int instructorsId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강사 이름")
    private String name;

    @Schema(description = "희망분야")
    private String field;

    @Schema(description = "신청 내용")
    private String text;

    @Schema(description = "권한 타입")
    private String userType;

    @Schema(description = "신청 상황")
    private String requestStatus;

    @Schema(description = "강사 신청일자", defaultValue = "현재 날짜와 시간", format = "date-time")
    private Date regdate;

}
