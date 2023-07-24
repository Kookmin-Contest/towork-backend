package com.backend.towork.member.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponse {

    private Long memberId;

    private String name;

    private String email;

    private String phoneNumber;

    private LocalDate birthDate;

    private LocalDateTime createDateTime;

}
