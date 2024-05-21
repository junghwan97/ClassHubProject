package com.example.classhubproject.controller.community;

import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.data.community.CommunityImageRequestDTO;
import com.example.classhubproject.data.community.CommunityRequestDTO;
import com.example.classhubproject.data.community.CommunityResponseDTO;
import com.example.classhubproject.data.community.PagingDTO;
import com.example.classhubproject.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @PostMapping("postBoard")
    public ResponseEntity<ResponseData<Void>> communityPost(@RequestBody CommunityRequestDTO communityRequestDTO) {
        //게시글 작성자, 제목, 내용, 게시글 유형 작성
        int cnt = communityService.posting(communityRequestDTO);

        if (cnt > 0) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.POST_COMMUNITY_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.POST_COMMUNITY_ERROR));
        }
    }

    @Operation(summary = "이미지 등록",
            description = "게시글 이미지를 등록할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 이미지 등록 성공", content = @Content(schema = @Schema(implementation = CommunityImageRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 이미지 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(value = "postImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<List<Integer>>> communityImagePost(@RequestPart("multipartFiles") List<MultipartFile> files) {

        //게시글 이미지 등록
        List<Integer> imageIds = communityService.postingImage(files);

        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.POST_COMMUNITY_IMAGE_SUCCESS, imageIds));
    }


    @Operation(summary = "질문게시판 목록 최신순 조회",
            description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions")
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectRecentList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> questions = communityService.questionsRecentList(page, search, type);
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
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectQuestionsListByFavorite(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                             @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                             @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> questions = communityService.questionsFavoriteList(page, search, type);
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
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectListByComment(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                   @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                   @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> questions = communityService.questionsCommentList(page, search, type);
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
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectStudiesRecentList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                       @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                       @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesRecentList(page, search, type);
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
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectStudiesListByFavorite(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                           @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                           @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesFavoriteList(page, search, type);
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
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectStudiesListByComment(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                          @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                          @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesCommentList(page, search, type);
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
    public ResponseEntity<ResponseData<PagingDTO<List<CommunityResponseDTO>>>> selectStudiesList(@RequestParam(value = "studyStatus") int status,
                                                                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                                                 @RequestParam(value = "search", defaultValue = "") String search,
                                                                                                 @RequestParam(value = "type", required = false) String type) {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesStatusList(status, page, search, type);
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
            description = "게시글 제목, 내용, 게시글 유형을 작성할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/board/{communityId}")
    public ResponseEntity<ResponseData<Void>> modifyBoard(@PathVariable("communityId") Integer communityId,
                                                          @RequestBody CommunityRequestDTO communityRequestDTO) {
        int cnt = communityService.modifyBoard(communityId, communityRequestDTO);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.MODIFY_COMMUNITY_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.MODIFY_COMMUNITY_ERROR));
        }
    }

    @Operation(summary = "게시글 이미지 삭제",
            description = "게시글 이미지를 삭제할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 이미지 삭제 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 이미지 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("delete/image")
    public ResponseEntity<ResponseData<Void>> deleteImage(@RequestParam(value = "removeFileId", required = false) List<Integer> removeFileId) {


        int cnt = communityService.deleteFiles(removeFileId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.DELETE_COMMUNITY_IMAGE_SUCCESS));
    }

    @Operation(summary = "게시글 이미지 재등록",
            description = "게시글 이미지를 다시 등록할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 이미지 재등록 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 이미지 재등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(value = "modify/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseData<Void>> modifyImage(@RequestParam("communityImageId") List<Integer> communityImageId,
                                                          @RequestPart("multipartFiles") List<MultipartFile> files) {

        int cnt = communityService.modifyFiles(communityImageId, files);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.POST_COMMUNITY_IMAGE_SUCCESS));
    }
}
