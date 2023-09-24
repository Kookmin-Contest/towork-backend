package com.towork.api.jwt.error;

import com.towork.api.global.error.exception.BusinessException;

public class TokenNotValidateException extends BusinessException {

    public TokenNotValidateException(String msg) {
        super(401, msg);
    }

}
