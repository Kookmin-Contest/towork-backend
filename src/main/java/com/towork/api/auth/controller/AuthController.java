package com.towork.api.auth.controller;

import com.towork.api.global.error.ErrorResponse;
import com.towork.api.auth.domain.dto.request.DuplicateEmailRequestDto;
import com.towork.api.auth.domain.dto.request.LoginRequestDto;
import com.towork.api.member.domain.dto.request.MemberRequestDto;
import com.towork.api.auth.domain.dto.request.ReissueRequestDto;
import com.towork.api.auth.domain.dto.response.TokenResponseDto;
import com.towork.api.auth.service.AuthService;
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
                    @ApiResponse(responseCode = "201", description = "성공적으로 멤버 정보가 저장됨."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
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
            }
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
                    @ApiResponse(responseCode = "200", description = "재발급 성공.", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<TokenResponseDto> reissue(@RequestBody @Valid ReissueRequestDto reissueRequestDto) {
        TokenResponseDto tokenResponseDto = authService.reissue(reissueRequestDto);
        return ResponseEntity.ok()
                .body(tokenResponseDto);
    }

    @PostMapping("/duplicate-email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "이메일 중복검사",
            description = "중복된 이메일이 있는지 없는지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "중복된 이메일이 없음.", content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 이메일이 형식이 맞지 않거나, 중복됨.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> validateDuplicateEmail(@RequestBody @Valid DuplicateEmailRequestDto duplicateEmailRequestDto) {
        authService.emailExists(duplicateEmailRequestDto.getEmail());
        return ResponseEntity.ok()
                .body(null);
    }

    // TODO: Implement Logout
}
