package com.backend.towork.global.handler;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.global.utils.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExpectedHandler implements ControllerHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return createErrorResponseEntity(ErrorCode.METHOD_ARGUMENT_NOT_VALID);
    }

}
