package com.backend.towork.workspace.service;

import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.workspace.domain.dto.request.WorkspaceRequestDto;
import com.backend.towork.workspace.domain.dto.response.WorkspaceResponseDto;
import com.backend.towork.workspace.domain.entity.Participant;
import com.backend.towork.workspace.domain.entity.Scope;
import com.backend.towork.workspace.domain.entity.Workspace;
import com.backend.towork.workspace.error.exception.WorkspaceNotFoundException;
import com.backend.towork.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    // TODO: workspace name으로 제한걸기, 개수 제한하기
    @Transactional
    public void createWorkspace(WorkspaceRequestDto workspaceRequestDto, Member member) {
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

    @PreAuthorize("@scopeService.hasUserScopeByWorkspaceId(#workspaceId, #member.id)")
    public WorkspaceResponseDto getWorkspaceInfoByWorkspaceId(Long workspaceId, Member member) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(WorkspaceNotFoundException::new);

        return WorkspaceResponseDto.builder()
                .id(workspace.getId())
                .workspaceName(workspace.getName())
                .build();
    }

}
