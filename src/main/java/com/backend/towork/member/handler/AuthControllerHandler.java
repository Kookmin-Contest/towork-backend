package com.backend.towork.member.handler;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.global.handler.ControllerHandler;
import com.backend.towork.global.utils.ErrorCode;
import com.backend.towork.member.controller.AuthController;
import com.backend.towork.member.handler.exception.EmailExistsException;
import com.backend.towork.member.handler.exception.InvalidEmailPasswordException;
import com.backend.towork.member.handler.exception.InvalidRefreshToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = AuthController.class)
public class AuthControllerHandler implements ControllerHandler {

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ErrorResponse> emailExistsException(EmailExistsException e) {
        return createErrorResponseEntity(ErrorCode.DUPLICATE_EMAIL);
    }

    @ExceptionHandler(InvalidEmailPasswordException.class)
    public ResponseEntity<ErrorResponse> invalidEmailPasswordException(InvalidEmailPasswordException e) {
        return createErrorResponseEntity(ErrorCode.INVALID_EMAIL_PASSWORD);
    }

    @ExceptionHandler(InvalidRefreshToken.class)
    public ResponseEntity<ErrorResponse> invalidRefreshToken(InvalidRefreshToken e) {
        return createErrorResponseEntity(ErrorCode.INVALID_REFRESH_TOKEN);
    }

}
