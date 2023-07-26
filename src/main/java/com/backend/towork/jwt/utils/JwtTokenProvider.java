package com.backend.towork.jwt.utils;

import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.service.PrincipalDetailService;
import io.jsonwebtoken.*;
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

    /**
     * TODO: throw error and handle it
     * 해당 error를 handling하기 위해선 AuthenticationEntryPoint라는 것을 사용해서
     * Filter 단에서 에러를 처리해야 합니다!!
     */
    public boolean validateToken(String token, Key secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

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
