package com.backend.towork.member.handler.exception;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException() {
        super("이미 존재하는 이메일입니다.");
    }

}
