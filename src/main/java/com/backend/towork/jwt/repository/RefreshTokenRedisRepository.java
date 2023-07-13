package com.backend.towork.jwt.repository;

import com.backend.towork.jwt.domain.RefreshToken;
import org.springframework.data.repository.Repository;

public interface RefreshTokenRedisRepository extends Repository<RefreshToken, Long> {

    void save(RefreshToken refreshToken);
    RefreshToken findByUsername(String username);
}
