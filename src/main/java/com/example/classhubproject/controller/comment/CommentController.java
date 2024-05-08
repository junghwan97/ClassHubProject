package com.example.classhubproject.controller.comment;

import com.example.classhubproject.data.comment.CommentDto;
import com.example.classhubproject.data.comment.CommentModifyDto;
import com.example.classhubproject.data.comment.CommentPostDto;
import com.example.classhubproject.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "게시물 댓글 조회", description = "상세 게시글의 댓글을 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("list")
    public List<CommentDto> commentList(@RequestParam("community") Integer communityId) {

        return commentService.commentList(communityId);
    }

    @Operation(summary = "게시물 댓글 등록", description = "상세 게시글에 댓글을 등록할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 성공", content = @Content(schema = @Schema(implementation = CommentPostDto.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("post")
    public String commentPost(@RequestBody CommentPostDto commentPostDto) {
        int cnt = commentService.commentPost(commentPostDto);
        if (cnt == 1) {
            System.out.println("댓글이 등록되었습니다.");
            return "댓글이 등록되었습니다.";
        } else {
            System.out.println("댓글 등록에 실패하였습니다.");
            return "댓글 등록에 실패하였습니다.";
        }
    }

    @Operation(summary = "게시물 댓글 삭제", description = "상세 게시글의 특정 댓글을 삭제할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("delete/id/{id}")
    public String commentDelete(@PathVariable("id") int commentId) {
        int cnt = commentService.commentDelete(commentId);
        if (cnt == 1) {
            System.out.println("댓글이 삭제되었습니다.");
            return "댓글이 삭제되었습니다";
        } else {
            System.out.println("댓글 삭제중 문제가 발생하였습니다.");
            return "댓글 삭제중 문제가 발생하였습니다.";
        }
    }

    @Operation(summary = "특정 댓글 조회", description = "상세 게시글의 특정 댓글을 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentDto.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("id/{id}")
    public CommentDto getComment(@PathVariable("id") int commentId) {
        return commentService.getComment(commentId);
    }

    @Operation(summary = "게시물 댓글 수정", description = "상세 게시글의 특정 댓글을 수정할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentModifyDto.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/{id}")
    public String commentModify(@PathVariable("id") int commentId, @RequestBody CommentModifyDto commentModifyDto) {

        int cnt = commentService.commentModify(commentId, commentModifyDto);
        if (cnt == 1) {
            return "댓글이 수정되었습니다.";
        } else {
            return "댓글 수정을 실패하였습니다.";
        }
    }
}

