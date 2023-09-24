package com.towork.api.global.error.handler;

import com.towork.api.global.error.exception.BusinessException;
import com.towork.api.global.error.ErrorResponse;
import com.towork.api.jwt.error.TokenNotValidateException;
import com.towork.api.jwt.utils.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Key;

/**
 * Controller단과 Filter단에서 발생하는 모든 Exception을 Handling합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 인가 과정에 대한 ExceptionHandler
     * {@link DelegatingAccessDeniedHandler}에서
     * {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어옵니다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedHandler(final AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 인증 과정에 대한 ExceptionHandler
     * {@link JwtTokenProvider#validateToken(String, Key)}에서 발생한 오류가 try-catch되어
     * {@link jakarta.servlet.http.HttpServletRequest#setAttribute(String, Object)}로 Exception이 bingding되고,
     * 해당 Exception이 {@link DelegatingAuthenticationEntryPoint}에서 받아져
     * {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어옵니다.
     */
    @ExceptionHandler(TokenNotValidateException.class)
    public ResponseEntity<ErrorResponse> handlerTokenNotValidateException(final TokenNotValidateException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * {@link jakarta.validation.Valid} annotaion에 의해 validation이 실패했을경우
     * Controller 단에서 발생하여 Error가 넘어옵니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleExpectedException(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * @see BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleExpectedException(final BusinessException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getStatus())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 처리되지 않은 오류가 여기에서 모두 잡힙니다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnExpectedException(final Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build());
    }


}
