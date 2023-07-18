package com.backend.towork.jwt.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@RedisHash(value = "refresh_token")
public class RefreshToken {

    @Id
    private String username;

    private String refreshToken;

}
