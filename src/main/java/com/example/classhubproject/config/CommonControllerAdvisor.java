package com.example.classhubproject.config;

import com.example.classhubproject.exception.ClassHubException;
import org.springframework.core.MethodParameter;
import org.springframework.http.*;
import org.springframework.http.server.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CommonControllerAdvisor implements ResponseBodyAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(ClassHubException.class)
    public ResponseEntity<Object> handleClassHubException(ClassHubException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
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
