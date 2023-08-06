package com.backend.towork.auth.error.exception;

import com.backend.towork.global.error.exception.InvalidValueException;

public class AlreadyEmailExistException extends InvalidValueException {

    public AlreadyEmailExistException() {
        super("이미 존재하는 이메일입니다.");
    }

}
