package com.backend.towork.member.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class MemberRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,10}$")
    private String name;

    @Pattern(regexp = "^01[0179][0-9]{7,8}$")
    private String phoneNumber;

    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$")
    private String birthDate;

}
