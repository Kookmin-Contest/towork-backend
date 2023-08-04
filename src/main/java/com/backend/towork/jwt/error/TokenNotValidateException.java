package com.backend.towork.jwt.error;

import com.backend.towork.global.error.BusinessException;

public class TokenNotValidateException extends BusinessException {

    public TokenNotValidateException(String msg) {
        super(401, msg);
    }

}
