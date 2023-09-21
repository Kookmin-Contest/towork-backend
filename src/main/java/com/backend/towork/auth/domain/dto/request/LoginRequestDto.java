package com.backend.towork.auth.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LoginRequestDto {

        @Schema(example = "example@gmail.com")
        @Email
        @NotBlank
        private String email;

        @Schema(example = "secret")
        @NotBlank
        private String password;

}