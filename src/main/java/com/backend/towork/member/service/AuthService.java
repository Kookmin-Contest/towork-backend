package com.backend.towork.member.service;

import com.backend.towork.global.handler.exception.ExpectedException;
import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.repository.RefreshTokenRepository;
import com.backend.towork.jwt.utils.JwtTokenKeys;
import com.backend.towork.jwt.utils.JwtTokenProvider;
import com.backend.towork.member.domain.dto.request.LoginRequest;
import com.backend.towork.member.domain.dto.request.MemberRequest;
import com.backend.towork.member.domain.dto.request.ReissueRequest;
import com.backend.towork.member.domain.dto.response.TokenResponse;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.Role;
import com.backend.towork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    private void saveRefreshToken(String username, String refreshToken) {
        refreshTokenRepository.save(RefreshToken.builder().username(username).refreshToken(refreshToken).build());
    }

    private boolean emailExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void signUp(final MemberRequest memberRequest) {
        if (emailExists(memberRequest.getEmail())) {
            throw new ExpectedException(400, "이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(memberRequest.getPassword());

        Member member = Member.builder()
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .name(memberRequest.getName())
                .birthDate(LocalDate.parse(memberRequest.getBirthDate(), DateTimeFormatter.ISO_DATE))
                .phoneNumber(memberRequest.getPhoneNumber())
                .role(Role.USER)
                .build();
        memberRepository.save(member);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ExpectedException(401, "잘못된 아이디 또는 패스워드 입니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new ExpectedException(401, "잘못된 아이디 또는 패스워드 입니다.");
        }

        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(email, refreshToken);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public TokenResponse reissue(ReissueRequest reissueRequest) {
        String refreshToken = reissueRequest.getRefreshToken();

        jwtTokenProvider.validateToken(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);

        String email = jwtTokenProvider.extractEmail(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);
        RefreshToken refreshTokenInRedis = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new ExpectedException(401, "DB 내에 해당 토큰이 존재하지 않습니다."));

        if (!refreshToken.equals(refreshTokenInRedis.getRefreshToken())) {
            throw new ExpectedException(401, "주어진 refresh token과 DB의 refresh token이 일치하지 않습니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        String reissuedAccessToken = jwtTokenProvider.generateToken(member);
        String reissuedRefreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(email, reissuedRefreshToken);

        return TokenResponse.builder().accessToken(reissuedAccessToken).refreshToken(reissuedRefreshToken).build();
    }


}
