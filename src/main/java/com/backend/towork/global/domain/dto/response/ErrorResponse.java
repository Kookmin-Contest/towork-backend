package com.backend.towork.global.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    @Schema(example = "400", description = "에러가 난 http status를 나타냅니다.")
    private final int status;

    @Schema(description = "에러가 난 이유를 나타냅니다.")
    private final String message;

}
