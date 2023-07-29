package com.backend.towork.member.controller;

import com.backend.towork.member.dto.LoginDto;
import com.backend.towork.member.dto.RegisterDto;
import com.backend.towork.member.dto.RegisterResponseDto;
import com.backend.towork.member.dto.TokenResponseDto;
import com.backend.towork.member.service.AuthService;
import com.backend.towork.oauth.service.SocialLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SocialLoginService socialLoginService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterDto registerDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        RegisterResponseDto responseDto = authService.register(registerDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        TokenResponseDto responseDto = authService.login(loginDto.username(), loginDto.password());
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest request) throws Exception {
        TokenResponseDto responseDto = authService.reissue(request);
        return ResponseEntity.ok().body(responseDto);
    }




    // TODO: Implement Logout
}
