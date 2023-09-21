package com.backend.towork.jwt.utils;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenKeys {

    public static Key ACCESS_SECRET_KEY;
    public static Key REFRESH_SECRET_KEY;

    public JwtTokenKeys(
            @Value("${jwt.accessSecretKey}") String accessSecretKey,
            @Value("${jwt.refreshSecretKey}") String refreshSecretKey) {
        byte[] accessKeyBytes = Decoders.BASE64.decode(accessSecretKey);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshSecretKey);
        ACCESS_SECRET_KEY = Keys.hmacShaKeyFor(accessKeyBytes);
        REFRESH_SECRET_KEY = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

}
