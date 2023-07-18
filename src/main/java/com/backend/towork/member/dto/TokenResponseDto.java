package com.backend.towork.member.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(
        String accessToken,
        String refreshToken
) {

}
