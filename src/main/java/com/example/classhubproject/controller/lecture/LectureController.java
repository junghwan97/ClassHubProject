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

import java.util.Map;

@RestController
@RequestMapping("lecture")
public class LectureController {
    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }


    // 강의 업로드
    @Operation(summary = "강의 업로드", description = "강의를 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("upload")
    public LectureUploadedResponse upload(@RequestBody LectureUploadedRequest request) {
        return lectureService.upload(request);
    }

    // 강의 수정
    @Operation(summary = "강의 수정", description = "강의를 수정.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("edit")
    public LectureEditedResponse edit(@RequestBody LectureEditedRequest request) {
        return lectureService.edit(request);
    }

    // 강의 자료 업로드
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
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("editMaterial")
    public LectureMaterialEditedResponse editMaterial(@RequestBody LectureMaterialEditedRequest request) {
        return lectureService.editMaterial(request);
    }

}
