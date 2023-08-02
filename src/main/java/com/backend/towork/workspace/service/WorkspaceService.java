package com.backend.towork.workspace.service;

import com.backend.towork.global.handler.exception.ExpectedException;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.repository.MemberRepository;
import com.backend.towork.workspace.domain.dto.request.WorkspaceRequestDto;
import com.backend.towork.workspace.domain.entify.Participant;
import com.backend.towork.workspace.domain.entify.Scope;
import com.backend.towork.workspace.domain.entify.Workspace;
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
    private final MemberRepository memberRepository;

    @Transactional
    public void createWorkspace(Long memberId, WorkspaceRequestDto workspaceRequestDto) {
        log.info("id: {}", memberId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ExpectedException(400, "멤버 조회에 실패했습니다."));

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
}
