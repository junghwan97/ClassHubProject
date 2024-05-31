package com.example.classhubproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ClassHubErrorCode {

    ALREADY_CART_ERROR("이미 장바구니에 동일한 상품이 있습니다.", HttpStatus.CONFLICT),
    ALREADY_ORDERED_ERROR("이미 보유한 강의가 포함되어 있습니다.", HttpStatus.CONFLICT),
    HAS_PAYMENT_AMOUNT_MISMATCH("결제 금액 비일치", HttpStatus.CONFLICT),
    NO_DATA_FOUND("조회 데이터 없음", HttpStatus.NOT_FOUND),
    POST_COMMUNITY_ERROR("게시글 작성중 문제가 발생했습니다.", HttpStatus.BAD_REQUEST),
    MODIFY_COMMUNITY_ERROR("게시글 수정중 문제가 발생했습니다.", HttpStatus.BAD_REQUEST);
    private String message;
    private HttpStatus httpStatus;

    ClassHubErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
