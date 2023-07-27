package com.backend.towork.member.controller;

import com.backend.towork.member.domain.dto.request.LoginRequest;
import com.backend.towork.member.domain.dto.request.MemberRequest;
import com.backend.towork.member.domain.dto.request.ReissueRequest;
import com.backend.towork.member.domain.dto.response.TokenResponse;
import com.backend.towork.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @PostMapping(path = "/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "회원가입",
            description = "주어진 멤버 정보를 DB에 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "성공적으로 멤버 정보가 저장됨."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.")
            },
            security = {}
    )
    public ResponseEntity<?> signUp(@RequestBody @Valid final MemberRequest memberRequest) {
        authService.signUp(memberRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(null);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "로그인",
            description = "email과 password를 이용하여 로그인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.")
            },
            security = {}
    )
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "토큰 재발급",
            description = "refreshToken을 이용하여 accessToken과 refreshToken을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재발급 성공."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음."),
                    @ApiResponse(responseCode = "401", description = "주어진 토큰이 만료됬거나 문제가 있음.")
            },
            security = {}
    )
    public ResponseEntity<TokenResponse> reissue(@RequestBody @Valid ReissueRequest reissueRequest) {
        TokenResponse tokenResponse = authService.reissue(reissueRequest);
        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    // TODO: Implement Logout
}
