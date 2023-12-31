package com.towork.api.auth.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
}
