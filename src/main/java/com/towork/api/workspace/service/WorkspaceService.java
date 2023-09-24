package com.towork.api.workspace.service;

import com.towork.api.member.domain.entity.Member;
import com.towork.api.workspace.domain.dto.request.WorkspaceRequestDto;
import com.towork.api.workspace.domain.dto.response.WorkspaceResponseDto;
import com.towork.api.workspace.domain.entity.Participant;
import com.towork.api.workspace.domain.entity.Scope;
import com.towork.api.workspace.domain.entity.Workspace;
import com.towork.api.workspace.domain.mapper.WorkspaceMapper;
import com.towork.api.workspace.error.exception.WorkspaceNotFoundException;
import com.towork.api.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<WorkspaceResponseDto> getWorkspaceInfoByWorkspaceId(List<Long> workspaceIds) {
        List<WorkspaceResponseDto> workspaceResponseDtoList =
                new ArrayList<>();

        workspaceIds.forEach(workspaceId -> {
            Workspace workspace = workspaceRepository.findById(workspaceId)
                            .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
            WorkspaceResponseDto workspaceResponseDto = WorkspaceMapper.INSTANCE.toResponseDto(workspace);
            workspaceResponseDtoList.add(workspaceResponseDto);
        });

        return workspaceResponseDtoList;
    }

}
