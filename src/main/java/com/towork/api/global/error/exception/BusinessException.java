package com.towork.api.global.error.exception;

import com.towork.api.global.error.handler.GlobalExceptionHandler;
import lombok.Getter;

/**
 * 요구사항에 맞지 않을 경우 발생시키는 Exception입니다.
 * {@link GlobalExceptionHandler}에서 해당 Exception이 Handling되므로
 * Service단에서 Exception을 발생시켜야 한다면 해당 Exception을 상속하여 사용하시면 됩니다!
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int status;

    public BusinessException(int status, String msg) {
        super(msg);
        this.status = status;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
