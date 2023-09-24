package com.towork.api.member.service;

import com.towork.api.member.domain.dto.request.NameUpdateRequestDto;
import com.towork.api.member.domain.dto.request.PhoneUpdateRequestDto;
import com.towork.api.member.domain.dto.response.MemberResponseDto;
import com.towork.api.member.domain.entity.Member;
import com.towork.api.member.repository.MemberRepository;
import com.towork.api.workspace.domain.dto.response.WorkspaceResponseDto;
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
                .countWorkspace(member.getCountWorkspace())
                .build();
    }

    @Transactional
    public void modifyName(NameUpdateRequestDto nameUpdateRequestDto, Member member) {
        member.changeName(nameUpdateRequestDto.getName());
    }

    @Transactional
    public void modifyPhone(PhoneUpdateRequestDto phoneUpdateRequestDto, Member member) {
        member.changePhoneNumber(phoneUpdateRequestDto.getPhoneNumber());
    }

    @Transactional
    public List<WorkspaceResponseDto> getWorkspacesOfMember(Member member) {
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
