package com.example.classhubproject.controller.comment;

import com.example.classhubproject.data.comment.CommentResponseDTO;
import com.example.classhubproject.data.comment.CommentRequestDTO;
import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "게시물 댓글 조회",
               description = "상세 게시글의 댓글을 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("list")
    public ResponseEntity<ResponseData<List<CommentResponseDTO>>> commentList(@RequestParam("community") Integer communityId) {

        List<CommentResponseDTO> commentList = commentService.commentList(communityId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.COMMENT_LIST_SUCCESS, commentList));
    }

    @Operation(summary = "게시물 댓글 등록",
               description = "상세 게시글에 댓글을 등록할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 성공", content = @Content(schema = @Schema(implementation = CommentRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("post")
    public ResponseEntity<ResponseData<Void>> commentPost(@RequestBody CommentRequestDTO commentRequestDTO) {
        int cnt = commentService.commentPost(commentRequestDTO);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.POST_COMMENT_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.POST_COMMENT_ERROR));
        }
    }

    @Operation(summary = "게시물 댓글 삭제",
               description = "상세 게시글의 특정 댓글을 삭제할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("delete/id/{id}")
    public ResponseEntity<ResponseData<Void>> commentDelete(@PathVariable("id") int commentId) {
        int cnt = commentService.commentDelete(commentId);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.DELETE_COMMENT_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.DELETE_COMMENT_ERROR));
        }
    }

    @Operation(summary = "특정 댓글 조회",
               description = "상세 게시글의 특정 댓글을 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("id/{id}")
    public ResponseEntity<ResponseData<CommentResponseDTO>> getComment(@PathVariable("id") int commentId) {
        CommentResponseDTO comment = commentService.getComment(commentId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.COMMENT_SUCCESS, comment));
    }

    @Operation(summary = "게시물 댓글 수정",
               description = "상세 게시글의 특정 댓글을 수정할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/{id}")
    public ResponseEntity<ResponseData<Void>> commentModify(@PathVariable("id") int commentId, @RequestBody CommentRequestDTO commentRequestDTO) {

        int cnt = commentService.commentModify(commentId, commentRequestDTO);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.MODIFY_COMMENT_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.MODIFY_COMMENT_ERROR));
        }
    }
}

