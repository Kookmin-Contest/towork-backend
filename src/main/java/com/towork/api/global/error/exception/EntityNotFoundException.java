package com.towork.api.global.error.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String msg) {
        super(HttpStatus.BAD_REQUEST.value(), msg);
    }

}
