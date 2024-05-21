package com.example.classhubproject.controller.cart;

import com.example.classhubproject.data.common.*;
import com.example.classhubproject.data.cart.*;
import com.example.classhubproject.service.cart.CartService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "장바구니 기능 모음", description = "장바구니 관련 기능을 처리")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    // 장바구니 추가
    @Operation(
            summary = "장바구니에 상품 추가",
            description = "장바구니에 상품을 추가합니다. 만약 이미 장바구니에 존재하는 상품이라면 상품 추가에 실패합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequestDTO.class))),
                    @ApiResponse(responseCode = "409", description = "이미 장바구니에 존재하는 상품", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequestDTO.class)))
            }
    )
    @PostMapping("/add")
    public ResponseEntity<ResponseData<Void>> addCart(@RequestBody @Schema(description = "회원 및 강의 ID", required = true, example = "{\"userId\": 1, \"classId\": 1}")
                                                      CartRequestDTO cartRequestDTO) {
        boolean addedToCart = cartService.addCart(cartRequestDTO);
        if (addedToCart) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.ADD_TO_CART_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.ALREADY_IN_CART));
        }
    }

    // 장바구니 목록
    @Operation(
            summary = "장바구니 목록",
            description = "특정 사용자의 장바구니에 있는 상품 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponseDTO.class)))
            }
    )
    @GetMapping("/list/{userId}")
    public ResponseEntity<ResponseData<List<CartResponseDTO>>> cartList(@PathVariable("userId") int userId) {
        List<CartResponseDTO> cartList = cartService.getCartList(userId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.CART_LIST_SUCCESS, cartList));
    }
    // 장바구니 개별 삭제
    @Operation(
            summary = "장바구니 개별 삭제",
            description = "장바구니에서 개별 상품을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "상품 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequestDTO.class))),
                    @ApiResponse(responseCode = "500", description = "상품 삭제 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequestDTO.class)))
            }
    )
    @PostMapping("/delete")
    public ResponseEntity<ResponseData<Void>> deleteCart(@RequestBody @Schema(description = "장바구니 ID", example = "{\"cartId\": 1}") CartRequestDTO cartRequestDTO) {
        boolean isDeleted = cartService.deleteCart(cartRequestDTO.getCartId());
        if (isDeleted) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.REMOVE_FROM_CART_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.DELETE_CART_ERROR));
        }
    }

    // 장바구니 비우기
    @Operation(
            summary = "장바구니 비우기",
            description = "특정 사용자의 장바구니를 비웁니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "장바구니 비우기 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequestDTO.class))),
                    @ApiResponse(responseCode = "500", description = "장바구니 비우기 실패", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartRequestDTO.class)))
            }
    )
    @PostMapping("/clear")
    public ResponseEntity<ResponseData<Void>> clearCart(@RequestBody @Schema(description = "회원 ID", example = "{\"userId\": 1}") CartRequestDTO cartRequestDTO) {
        boolean isCleared = cartService.clearCart(cartRequestDTO.getUserId());
        if (isCleared) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.CLEAR_CART_SUCCESS));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), ResponseMessage.CLEAR_CART_ERROR));
        }
    }

}
