package com.backend.towork.global.handler;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.global.utils.ErrorCode;
import org.springframework.http.ResponseEntity;

public interface ControllerHandler {

    default ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.getCode(), errorCode.getDescription()));
    }

}
