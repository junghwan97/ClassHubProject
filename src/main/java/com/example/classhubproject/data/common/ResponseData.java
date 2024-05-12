package com.example.classhubproject.data.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "API 응답을 표현하는 데이터 객체")
public class ResponseData<T> {

    @Schema(description = "HTTP 상태 코드")
    private int statusCode;

    @Schema(description = "응답 메시지")
    private String responseMessage;

    @Schema(description = "실제 데이터")
    private T data;

    public static <T> ResponseData<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static <T> ResponseData<T> res(final int statusCode, final String responseMessage, final T t) {
        return ResponseData.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}