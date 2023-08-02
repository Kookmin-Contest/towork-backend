package com.backend.towork.member.service;

import com.backend.towork.global.handler.exception.ExpectedException;
import com.backend.towork.member.domain.dto.request.NameUpdateRequestDto;
import com.backend.towork.member.domain.dto.request.PhoneUpdateRequestDto;
import com.backend.towork.member.domain.dto.response.MemberResponseDto;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.member.repository.MemberRepository;
import com.backend.towork.workspace.domain.dto.response.WorkspaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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

    @Transactional
    public List<WorkspaceResponseDto> getWorkspacesOfMember(PrincipalDetails principal) {
        Member member = memberRepository.findById(principal.getMember().getId())
                .orElseThrow(() -> new ExpectedException(400, "test"));

        List<WorkspaceResponseDto> workspaceResponseDtos =
                new ArrayList<>();
        member.getWorkspaces().forEach(workspaceEntity -> {
            workspaceResponseDtos.add(WorkspaceResponseDto.builder()
                    .id(workspaceEntity.getId())
                    .workspaceName(workspaceEntity.getName())
                    .build());
        });
        return workspaceResponseDtos;
    }
}
