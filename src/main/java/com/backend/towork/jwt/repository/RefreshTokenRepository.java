package com.backend.towork.jwt.repository;

import com.backend.towork.jwt.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
