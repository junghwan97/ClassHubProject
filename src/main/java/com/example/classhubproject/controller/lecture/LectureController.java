package com.example.classhubproject.controller.lecture;

import com.example.classhubproject.data.community.PagingDTO;
import com.example.classhubproject.data.lecture.*;
import com.example.classhubproject.data.oauth2.CustomOAuth2User;
import com.example.classhubproject.jwt.JWTUtil;
import com.example.classhubproject.mapper.user.UserMapper;
import com.example.classhubproject.service.lecture.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("lecture")
public class LectureController {
    private final LectureService lectureService;
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;

    @Autowired
    public LectureController(LectureService lectureService, UserMapper userMapper, JWTUtil jwtUtil) {
        this.lectureService = lectureService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
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

    // 강사 삭제
    @Operation(summary = "강사 삭제", description = "강사 삭제.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorAddedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("deleteInstructor")
    public String addInstructor(@RequestParam Integer userId) {
        return lectureService.deleteInstructor(userId);
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
    public PagingDTO<List<ClassResponseDTO>> selectAll(@RequestParam(required = false, name = "keyword")String keyword, @RequestParam(required = false, name ="page")Integer page){
    	if(StringUtils.hasText(keyword)) {
            PagingDTO<List<ClassResponseDTO>> res = lectureService.selectByKeyword(keyword, page);
            return res;
    	}else {
            PagingDTO<List<ClassResponseDTO>> res = lectureService.selectAll(page);
            return res;
        }
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

    // 강의 자료 업로드 / 수정
    @Operation(summary = "강의 썸네일 업로드", description = "강의 썸네일 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureMaterialUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
//    @PostMapping("uploadAndSyncMaterials")
//    public void uploadAndSyncMaterials(@RequestPart(name = "id") Integer id, @RequestPart(name = "files", required = false)List<MultipartFile> files) {
//
//    	lectureService.uploadAndSyncMaterials(id, files);
//    }

    @PostMapping("uploadThumnail/{classId}")
    public String uploadThumnail(@PathVariable("classId") Integer classId, @RequestPart(name = "thumnail", required = true)MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        return lectureService.uploadThumnail(classId, file);
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


     //강의 업로드 / 수정
    @Operation(summary = "강의 업로드", description = "강의 업로드.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureClassUploadedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    
    @PostMapping("uploadClass")
    public int uploadClass(@RequestPart(name = "request") LectureClassUploadedRequest request,
    													  @RequestPart(name = "sections") String sectionsJson,
    													  @RequestPart(required = false, name = "videos") List<MultipartFile> videos) throws IOException {
        return lectureService.uploadAndSyncClass(request,sectionsJson, videos);

    }

    // 카테고리 별 조회
    @Operation(summary = "강의 카테고리별 조회", description = "강의 카테고리별 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectByCategory")
    public PagingDTO<List<ClassResponseDTO>> selectByCategory(@RequestParam("categoryId")Integer categoryId, @RequestParam(required = false, name ="page")Integer page){
        PagingDTO<List<ClassResponseDTO>> res = lectureService.selectByCategory(categoryId, page);

        return res;
    }
    
    // 강의 추천 알고리즘
//    @Operation(summary = "강의 추천 알고리즘", description = "강의 추천 알고리즘.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
//                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            }
//    )
//    @GetMapping("recommendLectures")
//    public List<ClassResponseDTO> recommendLectures(@RequestBody ClassResponseDTO request){
//    	List<ClassResponseDTO> res = lectureService.recommendLectures(request);
//
//    	return res;
//    }

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

    @Operation(summary = "강의 업데이트용 조회", description = "강의 업데이트용 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("responseForUpdateVideo/{classId}")
    public ClassDetailResponseDTO responseForUpdateVideo(@PathVariable("classId") Integer classId){
        log.info("ddfadfadf");
        return lectureService.responseForUpdateVideo(classId);
    }

    @Operation(summary = "강의 업데이트용 조회", description = "강의 업데이트용 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("getsession")
    public String getsession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        return "ㅎㅇㅎㅇㅎ" + customOAuth2User.getUsername();
        //return userDTO.getUsername();
    }

    @Operation(summary = "내가 등록한 강의", description = "내가 등록한 강의 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/findClassByUserId/{userId}")
    public List<ClassResponseDTO> findClassByUserId(@PathVariable("userId") Integer userId){
        return lectureService.findClassByUserId(userId);
    }

    @Operation(summary = "내가 결제한 강의", description = "내가 결제한 강의 조회.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = LectureInstructorEditedResponse.class))),
                    @ApiResponse(responseCode = "400", description = "실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/findOrderClassByUserId/{userId}")
    public List<ClassResponseDTO> findOrderClassByUserId(@PathVariable("userId") Integer userId){
        return lectureService.findOrderClassByUserId(userId);
    }

//        Cookie[] cookies = request.getCookies();
//        String jwtToken = null;
//        for (Cookie cookie : cookies) {
//            if ("Authorization".equals(cookie.getName())) {
//                jwtToken = cookie.getValue();
//                break;
//            }
//        }
//        if (jwtToken != null) {
//            System.out.println("되라도릳리ㅏㄷ");
//            return userMapper.selectUserBySnsId(jwtUtil.getUsername(jwtToken));
//        }
//        return null;
//    }

}
