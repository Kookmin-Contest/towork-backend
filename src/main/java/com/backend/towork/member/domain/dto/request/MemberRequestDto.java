package com.backend.towork.member.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberRequestDto {

    @Schema(example = "example@gmail.com")
    @Email
    @NotBlank
    private String email;

    @Schema(example = "secret")
    @NotBlank
    private String password;

    @Schema(example = "홍길동")
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$")
    private String name;

    @Schema(example = "010-1234-5678")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-([0-9]{3,4})-([0-9]{4})$")
    private String phoneNumber;

    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})-(0[0-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String birthDate;

}
