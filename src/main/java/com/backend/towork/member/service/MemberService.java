package com.backend.towork.member.service;

import com.backend.towork.global.security.JwtTokenProvider;
import com.backend.towork.member.dto.LoginResponseDto;
import com.backend.towork.member.dto.RegisterDto;
import com.backend.towork.member.dto.RegisterResponseDto;
import com.backend.towork.member.entity.Member;
import com.backend.towork.member.entity.Role;
import com.backend.towork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponseDto register(RegisterDto registerDto) {
        if (memberRepository.findByUsername(registerDto.username()).isPresent()) {
            throw new IllegalStateException("동일한 이메일을 가진 아이디가 있습니다.");
        }

        String encodedPassword = passwordEncoder.encode(registerDto.password());

        Member member = Member.builder()
                .username(registerDto.username())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        memberRepository.save(member);
        return RegisterResponseDto.builder()
                .username(member.getUsername())
                .authority(member.getRole().name())
                .build();
    }

    @Transactional
    public LoginResponseDto login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String jwtToken = jwtTokenProvider.createToken(authentication);
        return LoginResponseDto.builder()
                .accessToken(jwtToken)
                .build();
    }
}
