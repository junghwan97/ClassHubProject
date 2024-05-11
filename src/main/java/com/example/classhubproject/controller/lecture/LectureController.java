package com.example.classhubproject.controller.lecture;

import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.service.lecture.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lecture")
public class LectureController {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    // 강사 업로드
    @Operation(summary = "강사 추가", description = "강사 추가.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorAddedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("addInstructor")
    public LectureInstructorAddedResponse addInstructor(@RequestBody LectureInstructorAddedRequest request) {
        return lectureService.addInstructor(request);
    }

    // 강사 수정
    @Operation(summary = "강사 수정", description = "강사 수정.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("editInstructor")
    public LectureInstructorEditedResponse editInstructor(@RequestBody LectureInstructorEditedRequest request) {
        return lectureService.editInstructor(request);
    }

    // 강의 자료 업로드 / 수정
    @Operation(summary = "강의 자료 업로드", description = "강의자료 를 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureMaterialUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("uploadMaterial")
    public LectureMaterialUploadedResponse uploadMaterial(@RequestBody LectureMaterialUploadedRequest request) {
        return lectureService.uploadMaterial(request);
    }

    @Operation(summary = "강의 자료 수정", description = "강의 자료 수정.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorAddedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("editMaterial")
    public LectureMaterialEditedResponse editMaterial(@RequestBody LectureMaterialEditedRequest request) {
        return lectureService.editMaterial(request);
    }

    // 강의 업로드 / 수정
    @Operation(summary = "강의 업로드", description = "강의 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureClassUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("uploadClass")
    public LectureClassUploadedResponse uploadClass(@RequestBody LectureClassUploadedRequest request) {
        return lectureService.uploadClass(request);
    }

    @Operation(summary = "강의 수정", description = "강의 수정.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureClassEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("editClass")
    public LectureClassEditedResponse editClass(@RequestBody LectureClassEditedRequest request) {
        return lectureService.editClass(request);
    }

}
