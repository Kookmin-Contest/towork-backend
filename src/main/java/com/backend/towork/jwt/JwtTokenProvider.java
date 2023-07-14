package com.backend.towork.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtTokenProvider {

    private final Key accessKey;
    private final Key refreshKey;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String GRANT_TYPE = "Bearer ";

    private static final long AT_EXPIRED_DURATION = 60 * 1000;
    private static final long RT_EXPIRED_DURATION = 60 * 60 * 1000;

    public JwtTokenProvider(@Value("${jwt.accessKey}") String accessSecretKey,
                            @Value("${jwt.refreshKey}") String refreshSecretKey) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(accessKey, SignatureAlgorithm.HS512)
                .setExpiration(new Date(now + AT_EXPIRED_DURATION))
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        long now = (new Date()).getTime();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
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

    public boolean isValidToken(String token, String tokenType) {
        Key signingkey;
        if (tokenType.equals("refresh")) {
            signingkey = refreshKey;
        }
        else {
            signingkey = accessKey;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingkey)
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

    public Authentication getAuthentication(String token, String tokenType) {
        Key signingkey;
        if (tokenType.equals("refresh")) {
            signingkey = refreshKey;
        }
        else {
            signingkey = accessKey;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingkey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(", "))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}
