package com.backend.towork.member.controller;

import com.backend.towork.member.dto.LoginDto;
import com.backend.towork.member.dto.RegisterDto;
import com.backend.towork.member.dto.RegisterResponseDto;
import com.backend.towork.member.dto.TokenResponseDto;
import com.backend.towork.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

    @GetMapping("/social-login")
    public ResponseEntity<?>  socialLogin(@RequestParam(value = "access_token", required = false) String accessToken,
                                          @RequestParam(value = "refresh_token", required = false) String refreshToken,
                                          @RequestParam(value = "error", required = false) String errorMessage) {
        if (errorMessage == null) {
            TokenResponseDto responseDto = TokenResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok().body(responseDto);
        }
        else {
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest request) throws Exception {
        TokenResponseDto responseDto = authService.reissue(request);
        return ResponseEntity.ok().body(responseDto);
    }

    // TODO: Implement Logout
}
