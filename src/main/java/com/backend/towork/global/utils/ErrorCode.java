package com.backend.towork.global.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    METHOD_ARGUMENT_NOT_VALID(400, "COMMON-001", "보낸 정보가 유효성 검증에 실패한 경우"),

    DUPLICATE_EMAIL(400, "MEMBER-001", "이메일이 중복된 경우"),
    INVALID_EMAIL_PASSWORD(403, "MEMBER-002", "이메일 또는 패스워드가 잘못된 경우"),
    INVALID_REFRESH_TOKEN(403, "MEMBER-003", "refresh token이 잘못된 경우"),

    MALFORMED_TOKEN(403, "JWT-001", "잘못된 JWT 서명일 경우"),
    EXPIRED_TOKEN(403, "JWT-002", "만료된 JWT 토큰일 경우"),
    UNSUPPORTED_TOKEN(403, "JWT-003", "지원되지 않는 JWT 토큰일 경우");

    private final int status;
    private final String code;
    private final String description;

}