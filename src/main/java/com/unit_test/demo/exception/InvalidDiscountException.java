package com.unit_test.demo.exception;

import org.springframework.http.HttpStatus;

public class InvalidDiscountException extends ApiException {

    public InvalidDiscountException(String message) {
        super(HttpStatus.BAD_REQUEST, "INVALID_DISCOUNT", message);
    }
}
