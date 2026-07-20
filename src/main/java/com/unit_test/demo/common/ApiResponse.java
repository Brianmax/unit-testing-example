package com.unit_test.demo.common;

import java.time.Instant;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;
    private final Instant timestamp;
    private final String path;

    private ApiResponse(boolean success, T data, ApiError error, String path) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = Instant.now();
        this.path = path;
    }

    public static <T> ApiResponse<T> success(T data, String path) {
        return new ApiResponse<>(true, data, null, path);
    }

    public static <T> ApiResponse<T> error(ApiError error, String path) {
        return new ApiResponse<>(false, null, error, path);
    }
}
