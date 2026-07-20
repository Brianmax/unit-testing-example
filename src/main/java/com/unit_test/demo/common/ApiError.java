package com.unit_test.demo.common;

import java.util.Map;

import lombok.Getter;

@Getter
public class ApiError {

    private final String code;
    private final String message;
    private final Map<String, String> details;

    public ApiError(String code, String message, Map<String, String> details) {
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
