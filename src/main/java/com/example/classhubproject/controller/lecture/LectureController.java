package com.example.classhubproject.controller.lecture;

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
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
    public void addInstructor(@RequestBody LectureInstructorAddedRequest request) {
        lectureService.addInstructor(request);

    }

    // 강사 수정
    @Operation(summary = "강사 수정", description = "강사 수정.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("editInstructor")
    public int editInstructor(@RequestBody LectureInstructorEditedRequest request) {
        log.info("로그안뜨는데요");
        return lectureService.editInstructor(request);
    }
    
    // 강의 조회 + 키워드 조회
    @Operation(summary = "강의 조회 + 키워드 조회", description = "강의 조회 + 키워드 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectAll")
    public List<ClassResponseDTO> selectAll(@RequestParam(required = false, name = "keyword")String keyword){
    	if(StringUtils.hasText(keyword)) {
    		List<ClassResponseDTO> res = lectureService.selectByKeyword(keyword);
    		if(!res.isEmpty()) {
    			return res;
    		}
    	}else {	
    		List<ClassResponseDTO> res = lectureService.selectAll();
	    	if(!res.isEmpty()) {
	    		return res;
	        }
    	}
        return null;
    }

    // 강의 1개 조회
    @Operation(summary = "강의 1개 조회", description = "강의 1개 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectById/{classId}")
    public Map<String, Object> selectById(@PathVariable("classId") Integer classId){
        return lectureService.selectById(classId);
    }

    // 강의 자료 업로드 / 수정 이것도 파일 업로드로 바꿔야함
    @Operation(summary = "강의 자료 업로드", description = "강의자료를 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureMaterialUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("uploadMaterial")
    public void uploadMaterial(@RequestPart(name = "id") Integer id, @RequestPart(name = "files")List<MultipartFile> files) {
        
    	lectureService.uploadMaterial(id, files);
    }

    // 강의 자료 조회
    @Operation(summary = "강의 자료 조회", description = "강의자료를 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureMaterialUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectMaterial")
    public List<LectureMaterialUploadedRequest> uploadMaterial(@RequestParam("classId")Integer classId) {

    	List<LectureMaterialUploadedRequest> res = lectureService.selectMaterial(classId);
    	
    	return res;
    }
    
    //강의 자료 수정
//    @Operation(summary = "강의 자료 수정", description = "강의 자료 수정.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorAddedResponse.class))),
//                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            }
//    )
//    @PostMapping("editMaterial")
//    public LectureMaterialEditedResponse editMaterial(@RequestBody LectureMaterialEditedRequest request) {
//        return lectureService.editMaterial(request);
//    }

     //강의 업로드 / 수정
    @Operation(summary = "강의 업로드", description = "강의 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureClassUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    
    @PostMapping("uploadClass")
    public void uploadClass(@RequestPart(name = "request") LectureClassUploadedRequest request,
    													  @RequestPart(name = "sections") String sectionsJson,
    													  @RequestPart(required = false, name = "videos") List<MultipartFile> videos) throws JsonMappingException, JsonProcessingException {
        lectureService.uploadClass(request,sectionsJson, videos);

    }

//    @Operation(summary = "강의 수정", description = "강의 수정.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureClassEditedResponse.class))),
//                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            }
//    )
//    @PostMapping("editClass")
//    public LectureClassEditedResponse editClass(@RequestBody LectureClassEditedRequest request) {
//        return lectureService.editClass(request);
//    }

    // 카테고리 별 조회
    @Operation(summary = "강의 카테고리별 조회", description = "강의 카테고리별 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectByCategory")
    public List<ClassResponseDTO> selectByCategory(@RequestParam("categoryId")Integer categoryId){
    	List<ClassResponseDTO> res = lectureService.selectByCategory(categoryId);

        return res;
    }
    
    // 강의 추천 알고리즘
    @Operation(summary = "강의 추천 알고리즘", description = "강의 추천 알고리즘.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("recommendLectures")
    public List<ClassResponseDTO> recommendLectures(@RequestBody ClassResponseDTO request){
    	List<ClassResponseDTO> res = lectureService.recommendLectures(request);
    	
    	return res;
    }

    // 관심 강의 등록
    @Operation(summary = "관심 강의 등록", description = "관심 강의 등록.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("favoriteLecture")
    public void favoriteLecture(@RequestParam("classId") Integer classId){
        lectureService.favoriteLecture(classId);
    }

    // 관심 강의 해제
    @Operation(summary = "관심 강의 해제", description = "관심 강의 해제.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("clearFavoriteLecture")
    public void clearFavoriteLecture(@RequestParam("classId") Integer classId){
        lectureService.clearFavoriteLecture(classId);
    }

    //강의 정보 던져 주는 로직
    @Operation(summary = "강의 상세 보기", description = "강의 상세 보기.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectClassDetail/{classId}")
    public List<List<LectureClassDetailDTO>> selectClassDetail(@PathVariable("classId") Integer classId){
        return lectureService.selectClassDetail(classId);
    }

    //학습 진행도 불러오기
    @Operation(summary = "학습 진행도 불러오기", description = "학습 진행도 불러오기",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectLearningData/{classDetailId}")
    public LearningDataDTO selectLearningData(@PathVariable("classDetailId") Integer classDetailId){
        return lectureService.selectLearningData(classDetailId);
    }

    //학습 진행도 전체 불러오기
    @Operation(summary = "학습 진행도 전체 불러오기", description = "학습 진행도 전체 불러오기",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectAllLearningData/{classId}")
    public List<LearningDataDTO> selectAllLearningData(@PathVariable("classId") Integer classId){
        return lectureService.selectAllLearningData(classId);
    }

    @Operation(summary = "강의 시청 기록", description = "강의 시청 기록.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("learningPoint")
    public void learningPoint(@RequestBody LearningDataDTO request){
        lectureService.learningPoint(request);
    }

    // 수강 신청 정보 생성
//    private void insertEnrollmentInfo(int ordersId) {
//        int userId = getUserId();
//
//        // 주문 상세의 class_id 조회
//        List<Integer> classIds = orderMapper.getClassIdByOrdersId(ordersId);
//
//        for (int classId : classIds) {
//            // 강의 별 수강료 조회
//            int enrollmentFee = lectureMapper.getClassPrice(classId);
//
//            enrollmentInfoMapper.insertEnrollmentInfo(userId, classId, enrollmentFee);
//
//            //lokyyyi
//            List<Integer> classDetailIds = lectureMapper.getClassDetailIds(classId);
//            for (Integer classDetailId : classDetailIds) {
//                LearningDataDTO request = new LearningDataDTO(userId, classDetailId);
//                lectureService.learningPoint(request);
//            }
//        }
//  }


}
