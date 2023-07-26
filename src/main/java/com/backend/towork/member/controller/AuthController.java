package com.backend.towork.member.controller;

import com.backend.towork.member.domain.dto.request.LoginRequest;
import com.backend.towork.member.domain.dto.request.MemberRequest;
import com.backend.towork.member.domain.dto.response.TokenResponse;
import com.backend.towork.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid final MemberRequest memberRequest) {
        authService.signUp(memberRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(null);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(HttpServletRequest request) throws Exception {
        TokenResponse tokenResponse = authService.reissue(request);
        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    // TODO: Implement Logout
}
