package com.backend.towork.member.service;

import com.backend.towork.member.domain.dto.request.NameUpdateRequestDto;
import com.backend.towork.member.domain.dto.request.PhoneUpdateRequestDto;
import com.backend.towork.member.domain.dto.response.MemberResponseDto;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDto getMemberInfo(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .createDateTime(member.getCreateDateTime())
                .build();
    }

    public void modifyName(NameUpdateRequestDto nameUpdateRequestDto, PrincipalDetails principal) {
        Member member = principal.getMember();
        member.changeName(nameUpdateRequestDto.getName());
        memberRepository.save(member);
    }

    public void modifyPhone(PhoneUpdateRequestDto phoneUpdateRequestDto, PrincipalDetails principal) {
        Member member = principal.getMember();
        member.changePhoneNumber(phoneUpdateRequestDto.getPhoneNumber());
        memberRepository.save(member);
    }

}
