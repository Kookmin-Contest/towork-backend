package com.backend.towork.jwt.repository;

import com.backend.towork.jwt.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(final RefreshToken refreshToken) {
        log.info("token: {}", refreshToken.getRefreshToken());
        log.info("username: {}", refreshToken.getUsername());
        redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), refreshToken.getUsername());
    }

    public Optional<String> findByRefreshToken(final String refreshToken) {
        String username = redisTemplate.opsForValue().get(refreshToken);

        if (username == null) {
            return Optional.empty();
        }

        return Optional.of(username);
    }

}
