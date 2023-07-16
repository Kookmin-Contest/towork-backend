package com.backend.towork.member.service;

import com.backend.towork.jwt.utils.JwtTokenProvider;
import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.utils.JwtTokenKeys;
import com.backend.towork.jwt.repository.RefreshTokenRepository;
import com.backend.towork.member.domain.Member;
import com.backend.towork.member.domain.Role;
import com.backend.towork.member.dto.RegisterDto;
import com.backend.towork.member.dto.RegisterResponseDto;
import com.backend.towork.member.dto.TokenResponseDto;
import com.backend.towork.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private void saveRefreshToken(String username, String refreshToken) {
        refreshTokenRepository.save(RefreshToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build());
    }

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

    public TokenResponseDto login(String username, String password) {
        authenticationManagerBuilder.getObject().authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(username, refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponseDto reissue(HttpServletRequest request) throws Exception {
        String refreshToken = jwtTokenProvider.resolveToken(request);

        jwtTokenProvider.validateToken(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);

        String username = jwtTokenProvider.extractUsername(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);
        RefreshToken refreshTokenInRedis = refreshTokenRepository.findById(username)
                .orElseThrow(() -> new Exception("redis 내에 주어진 username에 해당하는 token이 없습니다."));

        if (!refreshToken.equals(refreshTokenInRedis.getRefreshToken())) {
            throw new Exception("request의 refresh token과 DB의 refresh token과 일치하지 않습니다.");
        }

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        String reissuedAccessToken = jwtTokenProvider.generateToken(member);
        String reissuedRefreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(username, reissuedRefreshToken);

        return TokenResponseDto.builder()
                .accessToken(reissuedAccessToken)
                .refreshToken(reissuedRefreshToken)
                .build();
    }
}
