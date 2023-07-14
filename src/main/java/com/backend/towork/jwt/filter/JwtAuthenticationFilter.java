package com.backend.towork.jwt.filter;

import com.backend.towork.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private String tokenType = "access";

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        String excludeUri = "/reissue";

        if (requestUri.contains(excludeUri)) {
            tokenType = "refresh";
        }
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.isValidToken(token, tokenType)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token, tokenType);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 저장했습니다. uri: {}", authentication.getName(), requestUri);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다. uri: {}", requestUri);
        }

        filterChain.doFilter(request, response);
    }

}
