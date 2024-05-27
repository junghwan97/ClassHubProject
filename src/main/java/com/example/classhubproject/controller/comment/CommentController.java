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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 기능 모음", description = "댓글 관련 기능을 처리")
@RestController
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "게시물 댓글 조회",
               description = "상세 게시글의 댓글을 조회할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("list")
    public List<CommentResponseDTO> commentList(@RequestParam("community") Integer communityId) {

        List<CommentResponseDTO> commentList = commentService.commentList(communityId);
        return commentList;
    }

    @Operation(summary = "게시물 댓글 등록",
               description = "상세 게시글에 댓글을 등록할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 등록 성공", content = @Content(schema = @Schema(implementation = CommentRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("post")
    public void commentPost(@RequestBody CommentRequestDTO commentRequestDTO) {
        commentService.commentPost(commentRequestDTO);
    }

    @Operation(summary = "게시물 댓글 삭제",
               description = "상세 게시글의 특정 댓글을 삭제할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("delete/id/{id}")
    public void commentDelete(@PathVariable("id") int commentId) {
        commentService.commentDelete(commentId);
    }

//    @Operation(summary = "특정 댓글 조회",
//               description = "상세 게시글의 특정 댓글을 조회할 수 있습니다.",
//               responses = {
//                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentResponseDTO.class))),
//                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//            }
//    )
//    @GetMapping("id/{id}")
//    public CommentResponseDTO getComment(@PathVariable("id") int commentId) {
//        CommentResponseDTO comment = commentService.getComment(commentId);
//        return comment;
//    }

    @Operation(summary = "게시물 댓글 수정",
               description = "상세 게시글의 특정 댓글을 수정할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(schema = @Schema(implementation = CommentRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("modify/{id}")
    public void commentModify(@PathVariable("id") int commentId, @RequestBody CommentRequestDTO commentRequestDTO) {
        commentService.commentModify(commentId, commentRequestDTO);
    }
}

