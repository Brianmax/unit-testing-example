package com.unit_test.demo.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderStateException extends ApiException {

    public InvalidOrderStateException(String message) {
        super(HttpStatus.CONFLICT, "INVALID_ORDER_STATE", message);
    }
}
