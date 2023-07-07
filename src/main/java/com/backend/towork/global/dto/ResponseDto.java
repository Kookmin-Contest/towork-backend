package com.backend.towork.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class ResponseDto<D> {

    private final int resultCode;
    private final String message;
    private final D data;

}