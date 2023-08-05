package com.backend.towork.jwt.utils;

import com.backend.towork.jwt.error.TokenNotValidateException;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.service.PrincipalDetailService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String GRANT_TYPE = "Bearer ";
    private static final long AT_EXPIRED_DURATION = 60 * 1000;
    private static final long RT_EXPIRED_DURATION = 60 * 60 * 1000;
    private final PrincipalDetailService principalDetailService;

    public String generateToken(Member member) {
        return buildToken(member, JwtTokenKeys.ACCESS_SECRET_KEY, AT_EXPIRED_DURATION);
    }

    public String generateRefreshToken(Member member) {
        return buildToken(member, JwtTokenKeys.REFRESH_SECRET_KEY, RT_EXPIRED_DURATION);
    }

    private String buildToken(Member member, Key secretKey, long expiration) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(member.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANT_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void validateToken(String token, Key secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new TokenNotValidateException("토큰의 서명이 올바르지 않습니다.");
        } catch (MalformedJwtException e) {
            throw new TokenNotValidateException("토큰이 올바르지 않습니다.");
        } catch (ExpiredJwtException e) {
            throw new TokenNotValidateException("토큰이 만료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new TokenNotValidateException("토큰이 주어지지 않았습니다.");
        }
    }

    public Authentication getAuthentication(String token, Key secretKey) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = principalDetailService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    private Claims extractAllClaims(String token, Key secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, Key secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token, Key secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

}
