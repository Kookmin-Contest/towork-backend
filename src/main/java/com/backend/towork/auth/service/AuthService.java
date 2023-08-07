package com.backend.towork.auth.service;

import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.error.TokenNotValidateException;
import com.backend.towork.jwt.repository.RefreshTokenRepository;
import com.backend.towork.jwt.utils.JwtTokenKeys;
import com.backend.towork.jwt.utils.JwtTokenProvider;
import com.backend.towork.auth.domain.dto.request.LoginRequestDto;
import com.backend.towork.member.domain.dto.request.MemberRequestDto;
import com.backend.towork.auth.domain.dto.request.ReissueRequestDto;
import com.backend.towork.auth.domain.dto.response.TokenResponseDto;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.Role;
import com.backend.towork.auth.error.exception.AlreadyEmailExistException;
import com.backend.towork.auth.error.exception.InvalidEmailPasswordException;
import com.backend.towork.member.error.exception.MemberNotFoundException;
import com.backend.towork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    public void emailExists(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(m -> {
                    throw new AlreadyEmailExistException();
                });
    }

    @Transactional
    public void signUp(final MemberRequestDto memberRequestDto) {
        emailExists(memberRequestDto.getEmail());

        String encodedPassword = passwordEncoder.encode(memberRequestDto.getPassword());

        Member member = Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encodedPassword)
                .name(memberRequestDto.getName())
                .authProvider("TO-WORK")
                .birthDate(LocalDate.parse(memberRequestDto.getBirthDate(), DateTimeFormatter.ISO_DATE))
                .phoneNumber(memberRequestDto.getPhoneNumber())
                .role(Role.USER)
                .build();
        memberRepository.save(member);
    }

    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new InvalidEmailPasswordException();
        }

        String accessToken = jwtTokenProvider.generateToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(email, refreshToken);

        return TokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public TokenResponseDto reissue(ReissueRequestDto reissueRequestDto) {
        String refreshToken = reissueRequestDto.getRefreshToken();

        jwtTokenProvider.validateToken(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);

        String email = jwtTokenProvider.extractEmail(refreshToken, JwtTokenKeys.REFRESH_SECRET_KEY);
        RefreshToken refreshTokenInRedis = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new TokenNotValidateException("DB 내에 해당 토큰이 존재하지 않습니다."));

        if (!refreshToken.equals(refreshTokenInRedis.getRefreshToken())) {
            throw new TokenNotValidateException("주어진 refresh token과 DB의 refresh token이 일치하지 않습니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        String reissuedAccessToken = jwtTokenProvider.generateToken(member);
        String reissuedRefreshToken = jwtTokenProvider.generateRefreshToken(member);
        saveRefreshToken(email, reissuedRefreshToken);

        return TokenResponseDto.builder().accessToken(reissuedAccessToken).refreshToken(reissuedRefreshToken).build();
    }


}
