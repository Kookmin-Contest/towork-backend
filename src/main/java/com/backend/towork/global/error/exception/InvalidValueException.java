package com.backend.towork.global.error.exception;

import org.springframework.http.HttpStatus;

public class InvalidValueException extends BusinessException {

    public InvalidValueException(String msg) {
        super(HttpStatus.BAD_REQUEST.value(), msg);
    }

}
