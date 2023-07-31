package com.backend.towork.member.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class PhoneUpdateRequestDto {

    @NotBlank
    @Schema(example = "010-1234-5678")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$")
    private String phoneNumber;

}
