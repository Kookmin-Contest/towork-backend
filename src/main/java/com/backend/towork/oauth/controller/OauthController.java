package com.backend.towork.oauth.controller;

import com.backend.towork.member.dto.TokenResponseDto;
import com.backend.towork.oauth.service.SocialLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<TokenResponseDto> socialLogin(@PathVariable String registrationId, HttpServletRequest request) {
        TokenResponseDto tokenResponseDto = socialLoginService.login(registrationId, request.getParameter("code"));
        return ResponseEntity.ok().body(tokenResponseDto);
    }

}
