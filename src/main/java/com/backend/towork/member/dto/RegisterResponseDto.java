package com.backend.towork.member.dto;

import lombok.Builder;

@Builder
public record RegisterResponseDto(
        String username,
        String authority
) {
}
