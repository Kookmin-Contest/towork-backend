package com.backend.towork.member.handler.exception;

public class InvalidEmailPasswordException extends RuntimeException {

    public InvalidEmailPasswordException() {
        super("Email 또는 Password가 잘못되었습니다.");
    }

}
