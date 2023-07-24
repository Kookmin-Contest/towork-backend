package com.backend.towork.oauth2.handler;

import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.repository.RefreshTokenRepository;
import com.backend.towork.jwt.utils.JwtTokenProvider;
import com.backend.towork.member.principal.PrincipalDetails;
import com.backend.towork.oauth2.cookie.CookieAuthorizationRequestRepository;
import com.backend.towork.oauth2.cookie.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.backend.towork.oauth2.cookie.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("인증에 성공했습니다.");
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            log.info("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // TO DO : redirect uri 검증

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        log.info("targetUrl: " + targetUrl);

        String accessToken = jwtTokenProvider.generateToken((PrincipalDetails) authentication.getPrincipal());
        String refreshToken = jwtTokenProvider.generateRefreshToken((PrincipalDetails) authentication.getPrincipal());
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .username(authentication.getName())
                        .refreshToken(refreshToken)
                        .build()
        );

        log.info("AT: " + accessToken);
        log.info("RT: " + refreshToken);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
