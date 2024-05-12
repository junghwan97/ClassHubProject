package com.example.classhubproject.controller.community;

import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.data.community.CommunityRequestDTO;
import com.example.classhubproject.data.community.CommunityResponseDTO;
import com.example.classhubproject.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "커뮤니티 기능 모음", description = "커뮤니티 관련 기능을 처리")
@RestController
@RequestMapping("community")
public class CommunityController {

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Operation(summary = "게시글 등록",
               description = "게시글 작성자, 제목, 내용, 게시글 유형을 작성할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 등록 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("post")
    public ResponseEntity<ResponseData<Void>> communityPost(@RequestPart(value = "community", required = false) CommunityRequestDTO communityRequestDTO,
                                                            @RequestPart(value = "imageFiles", required = false) MultipartFile[] files) {

        //게시글 작성자, 제목, 내용, 게시글 유형 작성
        int cnt = communityService.posting(communityRequestDTO, files);

        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.POST_COMMUNITY_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.POST_COMMUNITY_ERROR));
        }
    }

    @Operation(summary = "질문게시판 목록 최신순 조회",
               description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectRecentList() {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> questions = communityService.questionsRecentList();
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.QUESTION_LIST_SUCCESS, questions));
    }

    @Operation(summary = "질문게시판 목록 좋아요순 조회",
               description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions/orderByFavorite")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectQuestionsListByFavorite( ) {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> questions = communityService.questionsFavoriteList();
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.QUESTION_LIST_SUCCESS, questions));
    }

    @Operation(summary = "질문게시판 목록 댓글 많은순 조회",
               description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions/orderByComment")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectListByComment() {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> questions = communityService.questionsCommentList();
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.QUESTION_LIST_SUCCESS, questions));
    }

    @Operation(summary = "스터디게시판 목록 최신순 조회",
               description = "스터디게시판(모집중, 모집완료) 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectStudiesRecentList() {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> studies = communityService.studiesRecentList();
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.STUDY_LIST_SUCCESS, studies));
    }

    @Operation(summary = "스터디게시판 목록 좋아요순 조회",
               description = "스터디게시판(모집중, 모집완료) 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies/orderByFavorite")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectStudiesListByFavorite() {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> studies = communityService.studiesFavoriteList();
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.STUDY_LIST_SUCCESS, studies));
    }

    @Operation(summary = "스터디게시판 목록 댓글 많은순 조회",
               description = "스터디게시판(모집중, 모집완료) 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies/orderByComment")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectStudiesListByComment() {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> studies = communityService.studiesCommentList();
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.STUDY_LIST_SUCCESS, studies));
    }

    @Operation(summary = "스터디게시판 상태로 목록 조회",
               description = "스터디게시판 모집중, 모집완료 중 하나의 상태의 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies/status")
    public ResponseEntity<ResponseData<List<CommunityResponseDTO>>> selectStudiesList(@RequestParam(value = "studyStatus") int status) {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        List<CommunityResponseDTO> studies = communityService.studiesStatusList(status);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.STUDY_LIST_SUCCESS, studies));
    }

    @Operation(summary = "질문게시글 상세 조회",
               description = "질문 게시글 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("question/{communityId}")
    public ResponseEntity<ResponseData<CommunityResponseDTO>> selectQuestion(@PathVariable("communityId") Integer id) {

        CommunityResponseDTO question = communityService.selectQuestion(id);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.QUESTION_SUCCESS, question));
    }

    @Operation(summary = "스터디게시글 상세 조회",
               description = "스터디 게시글 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("study/{communityId}")
    public ResponseEntity<ResponseData<CommunityResponseDTO>> selectStudy(@PathVariable("communityId") Integer id) {
        CommunityResponseDTO study = communityService.selectStudy(id);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.STUDY_SUCCESS, study));
    }

    @Operation(summary = "게시글 수정",
               description = "게시글 작성자, 제목, 일부 내용, 작성시간을 수정할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/{communityId}")
    public ResponseEntity<ResponseData<Void>> modifyCommunity(@PathVariable("communityId") Integer communityId,
                                  @RequestPart(value = "communityModify", required = false) CommunityRequestDTO communityDto,
                                  @RequestPart(value = "imageFiles", required = false) MultipartFile[] addFiles,
                                  @RequestPart(value = "removeFiles", required = false) List<String> removeFiles) {

        int cnt = communityService.modifyCommunity(communityId, communityDto, addFiles, removeFiles);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.MODIFY_COMMUNITY_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.MODIFY_COMMUNITY_ERROR));
        }
    }

}
