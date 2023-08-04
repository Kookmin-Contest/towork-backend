package com.backend.towork.global.handler;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.global.handler.exception.ExpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExpectedHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleUnExpectedException(final RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler(ExpectedException.class)
    public ResponseEntity<ErrorResponse> handleExpectedException(final ExpectedException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getStatus())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 인가 과정에 대한 ExceptionHandler
     * {@link com.backend.towork.global.handler.DelegatingAccessDeniedHandler}에서
     * {@link org.springframework.web.servlet.HandlerExceptionResolver}에 의해 Error가 넘어온다.
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleExpectedException(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage())
                        .build());
    }

}
