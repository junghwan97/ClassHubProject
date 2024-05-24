package com.example.classhubproject.controller.payment;

import com.example.classhubproject.data.common.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonControlerAdvisor implements ResponseBodyAdvice {

//    에러가 발생하면
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handelRunTimeExceptin(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseData.res(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        return ResponseEntity.ok(body);
    }
}
