package com.backend.towork.global.domain.dto.response;

import lombok.Getter;

@Getter
public class SuccessResponse extends BaseResponse {

    public SuccessResponse() {
        super("SUCCESS");
    }

}
