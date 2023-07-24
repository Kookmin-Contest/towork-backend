package com.backend.towork.global.domain.dto.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends BaseResponse {

    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        super("FAIL");
        this.code = code;
        this.message = message;
    }

}
