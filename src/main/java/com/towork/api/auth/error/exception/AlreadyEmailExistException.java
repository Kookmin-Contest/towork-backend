package com.towork.api.auth.error.exception;

import com.towork.api.global.error.exception.InvalidValueException;

public class AlreadyEmailExistException extends InvalidValueException {

    public AlreadyEmailExistException() {
        super("이미 존재하는 이메일입니다.");
    }

}
