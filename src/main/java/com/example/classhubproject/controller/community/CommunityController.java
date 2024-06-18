package com.example.classhubproject.controller.community;

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
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "커뮤니티 기능 모음", description = "커뮤니티 관련 기능을 처리")
@RestController
@RequiredArgsConstructor
@RequestMapping("community")
//@CrossOrigin(origins = "https://devproject.store")
public class CommunityController {

    private final CommunityService communityService;

    @Operation(summary = "게시글 등록",
            description = "게시글 작성자, 제목, 내용, 게시글 유형을 작성할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 등록 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("postBoard")
    @CrossOrigin(origins = "https://devproject.store")
    public void communityPost(@RequestBody CommunityRequestDTO communityRequestDTO) {
        communityService.posting(communityRequestDTO);
    }

    @Operation(summary = "이미지 등록",
            description = "게시글 이미지를 등록할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 이미지 등록 성공", content = @Content(schema = @Schema(implementation = CommunityImageRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 이미지 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(value = "postImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "https://devproject.store")
    public List<Integer> communityImagePost(@RequestPart("multipartFiles") List<MultipartFile> files) {
        //게시글 이미지 등록
        List<Integer> imageIds = communityService.postingImage(files);
        return imageIds;
    }


    @Operation(summary = "질문게시판 목록 최신순 조회",
            description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions")
    public PagingDTO<List<CommunityResponseDTO>> selectRecentList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "search", defaultValue = "") String search,
                                                                  @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> questions = communityService.questionsRecentList(page, search, type);
        return questions;
    }

    @Operation(summary = "질문게시판 목록 좋아요순 조회",
            description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions/orderByFavorite")
    public PagingDTO<List<CommunityResponseDTO>> selectQuestionsListByFavorite(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                               @RequestParam(value = "search", defaultValue = "") String search,
                                                                               @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> questions = communityService.questionsFavoriteList(page, search, type);
        return questions;
    }

    @Operation(summary = "질문게시판 목록 댓글 많은순 조회",
            description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions/orderByComment")
    public PagingDTO<List<CommunityResponseDTO>> selectListByComment(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                     @RequestParam(value = "search", defaultValue = "") String search,
                                                                     @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> questions = communityService.questionsCommentList(page, search, type);
        return questions;
    }

    @Operation(summary = "스터디게시판 목록 최신순 조회",
            description = "스터디게시판(모집중, 모집완료) 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies")
    public PagingDTO<List<CommunityResponseDTO>> selectStudiesRecentList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                         @RequestParam(value = "search", defaultValue = "") String search,
                                                                         @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesRecentList(page, search, type);
        return studies;
    }

    @Operation(summary = "스터디게시판 목록 좋아요순 조회",
            description = "스터디게시판(모집중, 모집완료) 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies/orderByFavorite")
    public PagingDTO<List<CommunityResponseDTO>> selectStudiesListByFavorite(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                             @RequestParam(value = "search", defaultValue = "") String search,
                                                                             @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesFavoriteList(page, search, type);
        return studies;
    }

    @Operation(summary = "스터디게시판 목록 댓글 많은순 조회",
            description = "스터디게시판(모집중, 모집완료) 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies/orderByComment")
    public PagingDTO<List<CommunityResponseDTO>> selectStudiesListByComment(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                            @RequestParam(value = "search", defaultValue = "") String search,
                                                                            @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesCommentList(page, search, type);
        return studies;
    }

    @Operation(summary = "스터디게시판 상태로 목록 조회",
            description = "스터디게시판 모집중, 모집완료 중 하나의 상태의 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies/status")
    public PagingDTO<List<CommunityResponseDTO>> selectStudiesList(@RequestParam(value = "studyStatus") int status,
                                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "search", defaultValue = "") String search,
                                                                   @RequestParam(value = "type", required = false) String type) {

        PagingDTO<List<CommunityResponseDTO>> studies = communityService.studiesStatusList(status, page, search, type);
        return studies;
    }

    @Operation(summary = "질문게시글 상세 조회",
            description = "질문 게시글 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("question/{communityId}")
    public CommunityResponseDTO selectQuestion(@PathVariable("communityId") Integer id) {

        CommunityResponseDTO question = communityService.selectQuestion(id);
        return question;
    }

    @Operation(summary = "스터디게시글 상세 조회",
            description = "스터디 게시글 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("study/{communityId}")
    public CommunityResponseDTO selectStudy(@PathVariable("communityId") Integer id) {
        CommunityResponseDTO study = communityService.selectStudy(id);
        return study;
    }

    @Operation(summary = "게시글 수정",
            description = "게시글 제목, 내용, 게시글 유형을 작성할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 수정 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/board/{communityId}")
    @CrossOrigin(origins = "https://devproject.store")
    public void modifyBoard(@PathVariable("communityId") Integer communityId,
                            @RequestBody CommunityRequestDTO communityRequestDTO) {
        communityService.modifyBoard(communityId, communityRequestDTO);
    }

    @Operation(summary = "게시글 이미지 삭제",
            description = "게시글 이미지를 삭제할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 이미지 삭제 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 이미지 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("delete/image")
    @CrossOrigin(origins = "https://devproject.store")
    public void deleteImage(@RequestParam(value = "removeFileId", required = false) List<Integer> removeFileId) {
        communityService.deleteFiles(removeFileId);
    }

    @Operation(summary = "메인 페이지 커뮤니티 조회",
            description = "메인페이지에서 커뮤니티 글을 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "메인페이지에 게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "메인페이지에 게시글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("mainpage")
    public List<CommunityResponseDTO> mainpageCommunity() {
        return communityService.selectCommunityForMainpage();
    }

//    @Operation(summary = "마이 페이지에서 내가 쓴 커뮤니티 질문 답변 게시글 조회",
//            description = "마이 페이지에서 내가 쓴 커뮤니티 글을 조회할 수 있습니다.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "질문게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
//                    @ApiResponse(responseCode = "400", description = "질문게시글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            }
//    )
//    @GetMapping("question/mypage")
//    public List<CommunityResponseDTO> mypageQuestionCommunity(@RequestParam(value = "userId") Integer userId) {
//        return communityService.selectQuestionForMypage(userId);
//    }
//
//    @Operation(summary = "마이 페이지에서 내가 쓴 커뮤니티 스터디 게시글 조회",
//            description = "마이 페이지에서 내가 쓴 커뮤니티 글을 조회할 수 있습니다.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
//                    @ApiResponse(responseCode = "400", description = "게시글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            }
//    )
//    @GetMapping("study/mypage")
//    public List<CommunityResponseDTO> mypageStudyCommunity(@RequestParam(value = "userId") Integer userId) {
//        return communityService.selectStduyForMypage(userId);
//    }

    @Operation(summary = "마이 페이지 커뮤니티글 조회",
            description = "마이 페이지에서 내가 쓴 커뮤니티 글을 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 조회 성공", content = @Content(schema = @Schema(implementation = CommunityRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("mypage")
    public List<CommunityResponseDTO> selectMypageCommunity(@RequestParam(value = "userId") Integer userId) {
        return communityService.selectMyCommunityByUserId(userId);
    }
}
