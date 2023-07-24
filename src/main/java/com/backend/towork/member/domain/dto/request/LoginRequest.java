package com.backend.towork.member.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

}