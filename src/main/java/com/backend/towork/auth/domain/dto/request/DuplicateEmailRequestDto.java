package com.backend.towork.auth.domain.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class DuplicateEmailRequestDto {

    @Email
    private String email;

}
