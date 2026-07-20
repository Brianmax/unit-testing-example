package com.unit_test.demo.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errorCode;

    protected ApiException(HttpStatus httpStatus, String errorCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
