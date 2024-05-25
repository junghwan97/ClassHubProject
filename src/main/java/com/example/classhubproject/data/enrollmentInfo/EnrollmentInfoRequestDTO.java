package com.example.classhubproject.data.enrollmentInfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "수강 신청 정보 Request DTO")
public class EnrollmentInfoRequestDTO {

    @Schema(description = "수강 신청 정보 고유 번호")
    private int enrollmentId;

    @Schema(description = "회원 ID")
    private int userId;

    @Schema(description = "강의 ID")
    private int classId;

}
