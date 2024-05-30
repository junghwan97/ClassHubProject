package com.example.classhubproject.exception;

import org.springframework.http.HttpStatus;

public class ClassHubException extends RuntimeException {

    private final ClassHubErrorCode errorCode;

    public ClassHubException(ClassHubErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }

}
