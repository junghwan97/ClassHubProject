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
    public static final String POST_COMMUNITY_SUCCESS = "게시글이 등록되었습니다";
    public static final String POST_COMMUNITY_ERROR = "게시글 등록 중 문제가 발생했습니다";
    public static final String QUESTION_LIST_SUCCESS = "질문 게시판 조회 성공";
    public static final String STUDY_LIST_SUCCESS = "스터디 게시판 조회 성공";
    public static final String QUESTION_SUCCESS = "스터디 게시물 조회 성공";
    public static final String STUDY_SUCCESS = "스터디 게시물 조회 성공";
    public static final String MODIFY_COMMUNITY_SUCCESS = "게시물이 수정되었습니다.";
    public static final String MODIFY_COMMUNITY_ERROR = "게시물 수정 중 문제가 발생했습니다.";
    public static final String COMMENT_LIST_SUCCESS = "게시물 댓글목록 조회 성공";
    public static final String COMMENT_SUCCESS = "게시물 댓글 조회 성공";
    public static final String POST_COMMENT_SUCCESS = "댓글이 등록되었습니다.";
    public static final String POST_COMMENT_ERROR = "댓글 등록 중 오류가 발생했습니다.";
    public static final String DELETE_COMMENT_SUCCESS = "댓글이 삭제되었습니다.";
    public static final String DELETE_COMMENT_ERROR = "댓글 삭제 중 문제가 발생했습니다.";
    public static final String MODIFY_COMMENT_SUCCESS = "댓글이 수정되었습니다.";
    public static final String MODIFY_COMMENT_ERROR = "댓글 수정 중 문제가 발생했습니다.";
    public static final String FAVORITE_INSERT_SUCCESS = "좋아요가 등록되었습니다.";
    public static final String FAVORITE_CANCEL_SUCCESS = "좋아요가 취소되었습니다.";


