package com.backend.towork.jwt.error;

import com.backend.towork.global.error.exception.BusinessException;

public class TokenNotValidateException extends BusinessException {

    public TokenNotValidateException(String msg) {
        super(401, msg);
    }

}
