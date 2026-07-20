package com.unit_test.demo.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.unit_test.demo.common.ApiError;
import com.unit_test.demo.common.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex, WebRequest request) {
        String path = requestPath(request);
        log.warn("Handled API exception: code={} status={} path={} message={}",
                ex.getErrorCode(), ex.getHttpStatus(), path, ex.getMessage());

        ApiError error = new ApiError(ex.getErrorCode(), ex.getMessage(), null);
        return ResponseEntity.status(ex.getHttpStatus()).body(ApiResponse.error(error, path));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex,
            WebRequest request) {
        String path = requestPath(request);
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Validation failed: path={} fields={}", path, details.keySet());

        ApiError error = new ApiError("VALIDATION_FAILED", "Input validation failed", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(error, path));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableMessage(HttpMessageNotReadableException ex,
            WebRequest request) {
        String path = requestPath(request);
        log.warn("Malformed request body: path={}", path);

        ApiError error = new ApiError("MALFORMED_REQUEST", "Request body is missing or malformed", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(error, path));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception ex, WebRequest request) {
        String path = requestPath(request);
        log.error("Unexpected error: path={}", path, ex);

        ApiError error = new ApiError("INTERNAL_ERROR", "An unexpected error occurred", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(error, path));
    }

    private String requestPath(WebRequest request) {
        String description = request.getDescription(false);
        return description.startsWith("uri=") ? description.substring(4) : description;
    }
}
