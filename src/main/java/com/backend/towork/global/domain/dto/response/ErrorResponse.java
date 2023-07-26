package com.backend.towork.global.domain.dto.response;

import lombok.Builder;

@Builder
public record ErrorResponse(int code, String message) {

}
