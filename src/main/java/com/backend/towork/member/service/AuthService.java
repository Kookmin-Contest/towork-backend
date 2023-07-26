package com.backend.towork.member.service;

import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.repository.RefreshTokenRepository;
import com.backend.towork.jwt.utils.JwtTokenKeys;
import com.backend.towork.jwt.utils.JwtTokenProvider;
import com.backend.towork.member.domain.dto.request.LoginRequest;
import com.backend.towork.member.domain.dto.request.MemberRequest;
import com.backend.towork.member.domain.dto.response.TokenResponse;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.Role;
import com.backend.towork.member.handler.exception.EmailExistsException;
import com.backend.towork.member.handler.exception.InvalidEmailPasswordException;
import com.backend.towork.member.handler.exception.InvalidRefreshToken;
import com.backend.towork.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            throw new EmailExistsException();
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
        Member member = memberRepository.findByEmail(email).orElseThrow(InvalidEmailPasswordException::new);

        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new InvalidEmailPasswordException();
        }

        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(email, refreshToken);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public TokenResponse reissue(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveToken(request);

        jwtTokenProvider.validateToken(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);

        String email = jwtTokenProvider.extractEmail(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);
        RefreshToken refreshTokenInRedis = refreshTokenRepository.findById(email).orElseThrow(() -> new InvalidRefreshToken("Redis DB 내에 해당 토큰이 존재하지 않습니다."));

        if (!refreshToken.equals(refreshTokenInRedis.getRefreshToken())) {
            throw new InvalidRefreshToken("주어진 refresh token과 DB의 refresh token이 일치하지 않습니다.");
        }

        Member member = memberRepository.findByEmail(email).orElseThrow(InvalidEmailPasswordException::new);

        String reissuedAccessToken = jwtTokenProvider.generateToken(member);
        String reissuedRefreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(email, reissuedRefreshToken);

        return TokenResponse.builder().accessToken(reissuedAccessToken).refreshToken(reissuedRefreshToken).build();
    }
}
