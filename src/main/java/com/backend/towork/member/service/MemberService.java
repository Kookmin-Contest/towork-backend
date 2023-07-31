package com.backend.towork.member.service;

import com.backend.towork.member.domain.dto.request.NameUpdateRequest;
import com.backend.towork.member.domain.dto.request.PhoneUpdateRequest;
import com.backend.towork.member.domain.dto.response.MemberResponse;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

    public void modifyName(NameUpdateRequest nameUpdateRequest, PrincipalDetails principal) {
        Member member = principal.getMember();
        member.changeName(nameUpdateRequest.getName());
        memberRepository.save(member);
    }

    public void modifyPhone(PhoneUpdateRequest phoneUpdateRequest, PrincipalDetails principal) {
        Member member = principal.getMember();
        member.changePhoneNumber(phoneUpdateRequest.getPhoneNumber());
        memberRepository.save(member);
    }

}
