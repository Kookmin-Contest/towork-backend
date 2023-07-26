package com.backend.towork.member.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class NameUpdateRequest {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$")
    private String name;

}
