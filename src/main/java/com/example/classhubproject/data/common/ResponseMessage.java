package com.example.classhubproject.data.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 응답 메시지")
public class ResponseMessage {

    // 장바구니 ResponseMessage
    public static final String ALREADY_IN_CART = "이미 장바구니에 담긴 상품입니다.";
    public static final String ADD_TO_CART_SUCCESS = "장바구니에 상품이 추가되었습니다.";
    public static final String CART_LIST_SUCCESS = "장바구니 목록 조회 성공";
    public static final String REMOVE_FROM_CART_SUCCESS = "장바구니에서 상품이 삭제되었습니다.";
    public static final String DELETE_CART_ERROR = "장바구니 상품 삭제 중 오류가 발생했습니다.";
    public static final String CLEAR_CART_SUCCESS = "장바구니가 비워졌습니다.";
    public static final String CLEAR_CART_ERROR = "장바구니 비우기 중 오류가 발생했습니다.";

}
