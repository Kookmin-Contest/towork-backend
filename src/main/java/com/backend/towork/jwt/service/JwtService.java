package com.backend.towork.jwt.service;

import com.backend.towork.jwt.domain.RefreshToken;
import com.backend.towork.jwt.repository.RefreshTokenRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    private final Key accessKey;
    private final Key refreshKey;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String GRANT_TYPE = "Bearer ";

    private static final String ACCESS_TOKEN_SUBJECT = "access_token";
    private static final String REFRESH_TOKEN_SUBJECT = "refresh_token";

    private static final String USERNAME_CLAIM = "username";

    private static final long AT_EXPIRED_DURATION = 60 * 60 * 1000;
    private static final long RT_EXPIRED_DURATION = 31 * 60 * 60 * 1000;

    public JwtService(@Value("${jwt.accessKey}") String accessSecretKey,
                      @Value("${jwt.refreshKey}") String refreshSecretKey, RefreshTokenRedisRepository refreshTokenRedisRepository) {
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String createAccessToken(String username) {
        long now = (new Date()).getTime();

        return Jwts.builder()
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .claim(USERNAME_CLAIM, username)
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + AT_EXPIRED_DURATION))
                .compact();
    }

    public String createRefreshToken() {
        long now = (new Date()).getTime();

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .signWith(refreshKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + RT_EXPIRED_DURATION))
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getUsernameByAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(accessKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public void saveRefreshToken(String username, String refreshToken) {
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .username(username).refreshToken(refreshToken)
                .build());
    }

}
