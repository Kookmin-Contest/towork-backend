package com.backend.towork.member.service;

import com.backend.towork.jwt.JwtTokenProvider;
import com.backend.towork.jwt.domain.RefreshToken;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
        refreshTokenRepository.save(RefreshToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build());

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponseDto reissue(HttpServletRequest request) throws Exception {
        String refreshToken = jwtTokenProvider.resolveToken(request);
        // TODO: 상황별 에러처리 필요
        String username = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(Exception::new);
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken, "refresh");
        String reissuedAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String reissuedRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenRepository.save(RefreshToken.builder()
                .username(username)
                .refreshToken(reissuedRefreshToken)
                .build());

        return TokenResponseDto.builder()
                .accessToken(reissuedAccessToken)
                .refreshToken(reissuedRefreshToken)
                .build();
    }
}
