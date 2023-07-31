package com.backend.towork.member.controller;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.member.domain.dto.request.LoginRequestDto;
import com.backend.towork.member.domain.dto.request.MemberRequestDto;
import com.backend.towork.member.domain.dto.request.ReissueRequestDto;
import com.backend.towork.member.domain.dto.response.TokenResponseDto;
import com.backend.towork.member.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
                    @ApiResponse(responseCode = "201", description = "성공적으로 멤버 정보가 저장됨.", content = @Content(schema = @Schema(implementation = MemberRequestDto.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = {}
    )
    public ResponseEntity<?> signUp(@RequestBody @Valid final MemberRequestDto memberRequestDto) {
        authService.signUp(memberRequestDto);
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
                    @ApiResponse(responseCode = "200", description = "로그인 성공.", content = @Content(schema = @Schema(implementation = LoginRequestDto.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = {}
    )
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok()
                .body(tokenResponseDto);
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "토큰 재발급",
            description = "refreshToken을 이용하여 accessToken과 refreshToken을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "재발급 성공.", content = @Content(schema = @Schema(implementation = ReissueRequestDto.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            },
            security = {}
    )
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody @Valid ReissueRequestDto reissueRequestDto) {
        TokenResponseDto tokenResponseDto = authService.reissue(reissueRequestDto);
        return ResponseEntity.ok()
                .body(tokenResponseDto);
    }




    // TODO: Implement Logout
}
