package com.example.classhubproject.controller.lecture;

import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.service.lecture.LectureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
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
    public ResponseEntity<ResponseData<Void>> addInstructor(@RequestBody LectureInstructorAddedRequest request) {
    	int count = lectureService.addInstructor(request);
    	
    	if(count>0) {
    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.INSERT_INSTRUCTOR_SUCCESS));
    	}
    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.INSERT_INSTRUCTOR_ERROR));
    }

    // 강사 수정
    @Operation(summary = "강사 수정", description = "강사 수정.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("editInstructor")
    public ResponseEntity<ResponseData<Void>> editInstructor(@RequestBody LectureInstructorEditedRequest request) {
        int count = lectureService.editInstructor(request);
        if(count > 0) {
        	return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.EDITE_INSTRUCTOR_SUCCESS));
        }
        	return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.EDITE_INSTRUCTOR_ERROR));
    	

    }
    
    // 강의 조회 + 키워드 조회
    @Operation(summary = "강의 조회 + 키워드 조회", description = "강의 조회 + 키워드 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectAll")
    public ResponseEntity<ResponseData<List<ClassResponseDTO>>> selectAll(@RequestParam(required = false, name = "keyword")String keyword){
    	if(StringUtils.hasText(keyword)) {
    		List<ClassResponseDTO> res = lectureService.selectByKeyword(keyword);
    		if(!res.isEmpty()) {
    			return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_SUCCESS, res));
    		}
    	}else {	
    		List<ClassResponseDTO> res = lectureService.selectAll();
	    	if(!res.isEmpty()) {
	    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_SUCCESS, res));
	        }
    	}
	    return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.LECTURE_ERROR));
    }
    

    // 강의 자료 업로드 / 수정 이것도 파일 업로드로 바꿔야함
    @Operation(summary = "강의 자료 업로드", description = "강의자료를 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureMaterialUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("uploadMaterial")
    public ResponseEntity<ResponseData<Void>> uploadMaterial(@RequestPart(name = "id") Integer id, @RequestPart(name = "files")List<MultipartFile> files) {
        
    	int res = lectureService.uploadMaterial(id, files);
    	
    	if(res>0) {
    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.LECTURE_MATERIAL_SUCCESS));    		
    	}
    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.LECTURE_MATERIAL_ERROR));
    	
    }

    // 강의 자료 조회
    @Operation(summary = "강의 자료 조회", description = "강의자료를 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureMaterialUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectMaterial")
    public ResponseEntity<ResponseData<List<LectureMaterialUploadedRequest>>> uploadMaterial(@RequestParam("classId")Integer classId) {

    	        
    	List<LectureMaterialUploadedRequest> res = lectureService.selectMaterial(classId);
    	
    	return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.LECTURE_MATERIAL_SUCCESS, res));    		
    	
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

     //강의 업로드 / 수정
    @Operation(summary = "강의 업로드", description = "강의 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureClassUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    
    @PostMapping("uploadClass")
    public ResponseEntity<ResponseData<Void>> uploadClass(@RequestPart(name = "request") LectureClassUploadedRequest request,
    													  @RequestPart(name = "sections") String sectionsJson,
    													  @RequestPart(required = false, name = "videos") List<MultipartFile> videos) throws JsonMappingException, JsonProcessingException {
        int res = lectureService.uploadClass(request,sectionsJson, videos);
    	
        if(res > 0) {
        	return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_INSERT_SUCCESS));
        }
        	return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.LECTURE_INSERT_ERROR));
    	
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
    
    // 카테고리 별 조회
    @Operation(summary = "강의 카테고리별 조회", description = "강의 카테고리별 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectByCategory")
    public ResponseEntity<ResponseData<List<ClassResponseDTO>>> selectByCategory(@RequestParam("categoryId")Integer categoryId){
    	List<ClassResponseDTO> res = lectureService.selectByCategory(categoryId);
    	
    	if(!res.isEmpty()) {
    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_SUCCESS, res));
        }
        	return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.LECTURE_ERROR));
    	
    }
    
 // 카테고리 별 조회
    @Operation(summary = "강의 추천 알고리즘", description = "강의 추천 알고리즘.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("recommendLectures")
    public ResponseEntity<ResponseData<List<ClassResponseDTO>>> recommendLectures(@RequestBody ClassResponseDTO request){
    	List<ClassResponseDTO> res = lectureService.recommendLectures(request);
    	
    	if(!res.isEmpty()) {
    		return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_SUCCESS, res));
        }
        	return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.LECTURE_ERROR));
    	
    }

    // 관심 강의 등록
    @Operation(summary = "관심 강의 등록", description = "관심 강의 등록.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("favoriteLecture")
    public ResponseEntity<ResponseData<Void>> favoriteLecture(@RequestParam("classId") Integer classId){
        if(lectureService.favoriteLecture(classId) > 0){
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_SUCCESS));
        }
            return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.LECTURE_ERROR));
    }

    // 관심 강의 해제
    @Operation(summary = "관심 강의 해제", description = "관심 강의 해제.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("clearFavoriteLecture")
    public ResponseEntity<ResponseData<Void>> clearFavoriteLecture(@RequestParam("classId") Integer classId){
        if(lectureService.clearFavoriteLecture(classId) > 0){
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(),ResponseMessage.LECTURE_SUCCESS));
        }
        return ResponseEntity.ok(ResponseData.res(HttpStatus.BAD_REQUEST.value(),ResponseMessage.LECTURE_ERROR));
    }

    //강의 정보 던져 주는 로직

    
//
//    @PostMapping("test")
//    public String test(@RequestPart(name = "request")LectureClassUploadedRequest request,
//			  @RequestPart(required = false, name = "videos") List<Object> videos) {
//    	if(videos.isEmpty()) {
//    		return "no";
//    	}
//    		return "yes";
//    }
}
