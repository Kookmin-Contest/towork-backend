package com.towork.api.member.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponseDto {

    @Schema(example = "1")
    private Long memberId;

    @Schema(example = "홍길동")
    private String name;

    @Schema(example = "example@gmail.com")
    private String email;

    @Schema(example = "010-1234-5678")
    private String phoneNumber;

    private LocalDate birthDate;

    private LocalDateTime createDateTime;

    private int countWorkspace;

}
