package com.backend.towork.jwt.domain;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@RedisHash(value = "refresh")
public class RefreshToken {

    @Id
    private String username;

    private String refreshToken;

}
