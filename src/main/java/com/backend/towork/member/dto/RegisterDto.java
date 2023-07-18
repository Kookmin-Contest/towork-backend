package com.backend.towork.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterDto(
        @NotNull
        @Size(min = 3, max = 20)
        String username,

        @NotNull
        @Size(min = 5, max = 100)
        String password
) {
}
