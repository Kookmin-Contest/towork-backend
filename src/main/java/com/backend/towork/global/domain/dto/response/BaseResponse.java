package com.backend.towork.global.domain.dto.response;

import lombok.Getter;

@Getter
public class BaseResponse {

    private String result;

    public BaseResponse(String result) {
        this.result = result;
    }

}
