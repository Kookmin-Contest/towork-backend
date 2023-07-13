package com.backend.towork.jwt.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@RedisHash
public class RefreshToken {

    @Id
    private String refreshToken;

    private String username;

}
