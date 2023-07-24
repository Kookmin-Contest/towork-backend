package com.backend.towork.member.service;

import com.backend.towork.member.domain.dto.response.MemberResponse;
import com.backend.towork.member.domain.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public MemberResponse getMemberInfo(Member member) {
        return MemberResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .createDateTime(member.getCreateDateTime())
                .build();
    }

}
