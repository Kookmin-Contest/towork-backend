package com.backend.towork.member.dto;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String accessToken
) {

}
