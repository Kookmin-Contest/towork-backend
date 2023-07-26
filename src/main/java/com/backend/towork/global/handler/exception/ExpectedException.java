package com.backend.towork.global.handler.exception;

import lombok.Getter;

@Getter
public class ExpectedException extends RuntimeException {

    private final int status;

    public ExpectedException(int status, String msg) {
        super(msg);
        this.status = status;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
