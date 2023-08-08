package com.backend.towork.global.error.handler;

import com.backend.towork.jwt.error.TokenNotValidateException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
public class DelegatingAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    public DelegatingAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        Exception exception = (Exception) request.getAttribute("exception");
        if (exception == null) {
            return;
        }

        if (exception instanceof TokenNotValidateException tokenNotValidateException) {
            resolver.resolveException(request, response, null, tokenNotValidateException);
        } else {
            // 처리되지 않은 exception이 발생한 경우입니다.
            log.error("{}: {}", exception.getClass(), exception.getMessage());
            resolver.resolveException(request, response, null, exception);
        }
    }
}
