package com.towork.api.auth.error.exception;

import com.towork.api.global.error.exception.InvalidValueException;

public class InvalidEmailPasswordException extends InvalidValueException {

    public InvalidEmailPasswordException() {
        super("이메일 또는 패스워드가 잘못되었습니다.");
    }

}
