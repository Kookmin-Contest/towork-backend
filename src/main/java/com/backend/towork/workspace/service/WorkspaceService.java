package com.backend.towork.workspace.service;

import com.backend.towork.global.handler.exception.ExpectedException;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.repository.MemberRepository;
import com.backend.towork.workspace.domain.dto.request.WorkspaceRequestDto;
import com.backend.towork.workspace.domain.dto.response.WorkspaceResponseDto;
import com.backend.towork.workspace.domain.entify.Participant;
import com.backend.towork.workspace.domain.entify.Scope;
import com.backend.towork.workspace.domain.entify.Workspace;
import com.backend.towork.workspace.repository.ParticipantRepository;
import com.backend.towork.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createWorkspace(Member member, WorkspaceRequestDto workspaceRequestDto) {
        Participant participant = Participant.builder()
                .member(member)
                .scope(Scope.OWNER)
                .build();
        Workspace workspace = Workspace.builder()
                .name(workspaceRequestDto.getWorkspaceName())
                .owner(member)
                .participants(List.of(participant))
                .build();
        participant.setWorkspace(workspace);

        workspaceRepository.save(workspace);
    }

    public WorkspaceResponseDto getWorkspace(Long workspaceId, Member member) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ExpectedException(400, "없는 워크스페이스 입니다."));

        participantRepository.getRoleByWorkspaceIdAndMemberId(workspaceId, member.getId())
                .orElseThrow(() -> new ExpectedException(403, "해당 워크스페이스를 가져올 권한이 없습니다."));

        return WorkspaceResponseDto.builder()
                .id(workspace.getId())
                .workspaceName(workspace.getName())
                .build();
    }

}
