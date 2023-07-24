package com.backend.towork.member.handler.exception;

public class InvalidRefreshToken extends RuntimeException {

    public InvalidRefreshToken(String msg) {
        super(msg);
    }

}
