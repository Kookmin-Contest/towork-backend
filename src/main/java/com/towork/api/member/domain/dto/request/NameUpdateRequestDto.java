package com.towork.api.member.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class NameUpdateRequestDto {

    @Schema(example = "홍길동")
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$")
    private String name;


}
