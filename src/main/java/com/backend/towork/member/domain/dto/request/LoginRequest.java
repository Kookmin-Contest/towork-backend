package com.backend.towork.member.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

        @Schema(example = "example@gmail.com")
        @Email
        @NotBlank
        private String email;

        @Schema(example = "secret")
        @NotBlank
        private String password;


}