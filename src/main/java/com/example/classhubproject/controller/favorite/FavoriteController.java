package com.example.classhubproject.controller.favorite;

import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.data.favorite.FavoriteRequestDTO;
import com.example.classhubproject.service.favorite.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // 게시글 좋아요 버튼 누를 때 기존에 좋아요 상태이면 취소 실행
    // 처음 좋아요 누르는 것이면 좋아요 등록
    @Operation(summary = "게시글 좋아요 등록",
               description = "상세 게시글에 좋아요를 등록할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 등록 성공", content = @Content(schema = @Schema(implementation = FavoriteRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "좋아요 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("community")
    public ResponseEntity<ResponseData<Void>> favoriteCommunity(@RequestBody FavoriteRequestDTO favorite) {
        int cnt = favoriteService.favoriteCommunity(favorite);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.FAVORITE_INSERT_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.FAVORITE_CANCEL_SUCCESS));
        }
    }

    // 댓글 좋아요 버튼 누를 때 기존에 좋아요 상태이면 취소 실행
    // 처음 좋아요 누르는 것이면 좋아요 등록
    @Operation(summary = "댓글 좋아요 등록",
               description = "특정 댓글에 좋아요를 등록할 수 있습니다.",
               responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 등록 성공", content = @Content(schema = @Schema(implementation = FavoriteRequestDTO.class))),
                    @ApiResponse(responseCode = "400", description = "좋아요 등록 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("comment")
    public ResponseEntity<ResponseData<Void>> favoriteComment(@RequestBody FavoriteRequestDTO favorite) {
        int cnt = favoriteService.favoriteComment(favorite);
        if (cnt == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.FAVORITE_INSERT_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.FAVORITE_CANCEL_SUCCESS));
        }
    }
}