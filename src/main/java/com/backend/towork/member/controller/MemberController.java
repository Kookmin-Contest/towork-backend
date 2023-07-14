package com.backend.towork.member.controller;

import com.backend.towork.member.dto.LoginDto;
import com.backend.towork.member.dto.RegisterDto;
import com.backend.towork.member.dto.RegisterResponseDto;
import com.backend.towork.member.dto.TokenResponseDto;
import com.backend.towork.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterDto registerDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        log.info("aaaaa");
        RegisterResponseDto responseDto = memberService.register(registerDto);
        log.info(responseDto.toString());
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        TokenResponseDto responseDto = memberService.login(loginDto.username(), loginDto.password());
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest request) throws Exception {
        TokenResponseDto responseDto = memberService.reissue(request);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/test")
    public String test() {
        return "TEST";
    }
}
