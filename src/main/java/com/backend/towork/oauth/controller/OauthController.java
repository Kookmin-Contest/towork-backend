package com.backend.towork.oauth.controller;

import com.backend.towork.member.domain.dto.response.TokenResponseDto;
import com.backend.towork.oauth.service.SocialLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth2")
public class OauthController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/social-login/{registrationId}")
    public void redirectLoginPage(HttpServletResponse response, @PathVariable String registrationId) throws IOException {
        String authorizationUriRequest = socialLoginService.getAuthorizationUriRequest(registrationId);
        response.sendRedirect(authorizationUriRequest);
    }

    @GetMapping("/callback/{registrationId}")
    public void socialLogin(@PathVariable String registrationId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TokenResponseDto tokenResponseDto = socialLoginService.login(registrationId, request.getParameter("code"));
        String callbackUrlScheme = UriComponentsBuilder.fromUriString("com.example.gotowork://oauth-callback")
                .queryParam("accessToken", tokenResponseDto.getAccessToken())
                .queryParam("refreshToken", tokenResponseDto.getRefreshToken())
                .build().toUriString();
        log.info(callbackUrlScheme);
        response.sendRedirect(callbackUrlScheme);
    }

}
