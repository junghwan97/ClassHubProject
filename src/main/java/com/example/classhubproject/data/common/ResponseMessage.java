package com.example.classhubproject.data.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "API 응답 메시지")
public class ResponseMessage {

    // 게시판요좋아요 ResponseMessage
    public static final String FAVORITE_INSERT_SUCCESS = "좋아요가 등록되었습니다.";
    public static final String FAVORITE_CANCEL_SUCCESS = "좋아요가 취소되었습니다.";
    // 강의 ResponseMessage
    public static final String EDITE_INSTRUCTOR_SUCCESS = "강사 정보 수정 성공";
    public static final String EDITE_INSTRUCTOR_ERROR = "강사 정보 수정 실패";
    public static final String INSERT_INSTRUCTOR_SUCCESS = "강사 정보 등록 성공";
    public static final String INSERT_INSTRUCTOR_ERROR = "강사 정보 등록 실패";
    public static final String LECTURE_INSERT_SUCCESS = "강의 등록 성공";
    public static final String LECTURE_INSERT_ERROR = "강의 등록 실패";
    public static final String LECTURE_SUCCESS = "강의 조회 성공";
    public static final String LECTURE_ERROR = "강의 조회 실패";
    public static final String LECTURE_MATERIAL_SUCCESS = "강의 등록 성공";
    public static final String LECTURE_MATERIAL_ERROR = "강의 등록 실패";
      // 회원 ResponseMessage
    public static final String JOIN_SUCCESS = "회원가입이 성공";
    public static final String JOIN_DUPLICATE = "이미 로그인된 회원입니다.";
    public static final String SELECT_USER = "회원 조회가 완료되었습니다.";
}

