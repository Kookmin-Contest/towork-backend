package com.backend.towork.jwt.filter;

import com.backend.towork.global.error.ErrorResponse;
import com.backend.towork.jwt.error.TokenNotValidateException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenNotValidateException e) {
            setErrorResponse(request, response, e);
        }
    }

    private void setErrorResponse(HttpServletRequest request,
                                  HttpServletResponse response,
                                  TokenNotValidateException e) throws IOException {
        response.setStatus(e.getStatus());
        response.setContentType("application/json; charset=UTF-8");

        response.getWriter().write(
                ErrorResponse.builder()
                        .status(e.getStatus())
                        .message(e.getMessage())
                        .build()
                        .convertToJson()
        );
    }

}
