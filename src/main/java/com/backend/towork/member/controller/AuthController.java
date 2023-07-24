package com.backend.towork.member.controller;

import com.backend.towork.global.domain.dto.response.DataResponse;
import com.backend.towork.global.domain.dto.response.SuccessResponse;
import com.backend.towork.member.domain.dto.request.LoginRequest;
import com.backend.towork.member.domain.dto.request.MemberRequest;
import com.backend.towork.member.domain.dto.response.TokenResponse;
import com.backend.towork.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse> signUp(@RequestBody @Valid final MemberRequest memberRequest) {
        authService.signUp(memberRequest);
        return ResponseEntity.ok()
                .body(new SuccessResponse());
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok()
                .body(new DataResponse<>(tokenResponse));
    }

    @PostMapping("/reissue")
    public ResponseEntity<DataResponse<TokenResponse>> reissue(HttpServletRequest request) throws Exception {
        TokenResponse tokenResponse = authService.reissue(request);
        return ResponseEntity.ok()
                .body(new DataResponse<>(tokenResponse));
    }

    // TODO: Implement Logout
}
