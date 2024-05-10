package com.example.classhubproject.controller.community;

import com.example.classhubproject.data.community.CommunityDto;
import com.example.classhubproject.data.community.CommunityModifyDto;
import com.example.classhubproject.data.community.CommunityRegDto;
import com.example.classhubproject.service.community.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("community")
public class CommunityController {

    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Operation(summary = "게시글 등록", description = "게시글 작성자, 제목, 내용, 게시글 유형을 작성할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시글 등록 성공", content = @Content(schema = @Schema(implementation = CommunityRegDto.class))),
                    @ApiResponse(responseCode = "400", description = "게시글 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("post")
    public String communityPost(@RequestPart(value = "community", required = false) CommunityRegDto communityRegDto,
                                @RequestPart(value = "imageFiles", required = false) MultipartFile[] files) throws Exception {

        //게시글 작성자, 제목, 내용, 게시글 유형 작성
        int cnt = communityService.posting(communityRegDto, files);
//        int cnt = communityService.posting(communityRegDto);

        // 게시글 사진 등록
//        for (MultipartFile multipartFile : files) {
//
//        }

        if (cnt == 1) {
            System.out.println("게시글이 등록되었습니다.");
            // 게시글 상세페이지로 리다이렉트
            // +++++++++++++++++++++++++++++++++++++++++++++++

            return "게시글이 등록되었습니다.";
        } else {
            System.out.println("게시글 등록에 실패하였습니다.");
            // 게시글 작성페이지로 리다이렉트
            // +++++++++++++++++++++++++++++++++++++++++++++++
            return "게시글 등록에 실패하였습니다";
        }
    }

    @Operation(summary = "질문게시판 목록 조회", description = "질문게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityDto.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("questions")
    public List<CommunityDto> selectList( /*@RequestParam(value = "page", defaultValue = "1") Integer page*/) {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        return communityService.QuestionsList();
    }

    @Operation(summary = "스터디게시판 목록 조회", description = "스터디게시판 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시판 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityDto.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시판 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("studies")
    public List<CommunityDto> selectStudiesList( /*@RequestParam(value = "page", defaultValue = "1") Integer page*/) {
        // 수정일자가 있으면 날짜에 작성날짜 대신 수정날짜 입력
        // +++++++++++++++++++++++++++++++++++++++++++++++

        return communityService.StudiesList();
    }

    @Operation(summary = "질문게시글 상세 조회", description = "질문 게시글 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "질문게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityDto.class))),
                    @ApiResponse(responseCode = "400", description = "질문게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("question/{communityId}")
    public CommunityDto selectQuestion(@PathVariable("communityId") Integer id) {

        return communityService.selectQuestion(id);
    }

    @Operation(summary = "스터디게시글 상세 조회", description = "스터디 게시글 작성자, 제목, 일부 내용, 작성시간, 좋아요 수, 댓글 수를 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityDto.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("study/{communityId}")
    public CommunityDto selectStudy(@PathVariable("communityId") Integer id) {
        return communityService.selectStudy(id);
    }

    @Operation(summary = "게시글 수정", description = "게시글 작성자, 제목, 일부 내용, 작성시간을 수정할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스터디게시글 목록 조회 성공", content = @Content(schema = @Schema(implementation = CommunityDto.class))),
                    @ApiResponse(responseCode = "400", description = "스터디게시글 목록 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/{communityId}")
    public String modifyCommunity(@PathVariable("communityId") Integer communityId, @RequestBody CommunityModifyDto communityModifyDto) {

        int cnt = communityService.modifyCommunity(communityId, communityModifyDto);
        if (cnt == 1) {
            System.out.println("수정이 완료되었습니다.");
            return "수정이 완료되었습니다.";
        } else {
            System.out.println("수정중 문제가 발생하였습니다.");
            return "수정중 문제가 발생하였습니다.";
        }
    }

}
