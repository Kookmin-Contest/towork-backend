package com.backend.towork.global.service;

import com.backend.towork.workspace.domain.entity.Scope;
import com.backend.towork.workspace.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScopeService {

    private final ParticipantRepository participantRepository;

    public boolean hasUserScopeByWorkspaceId(Long workspaceId, Long memberId) {
        Optional<Scope> optionalScope = participantRepository.getScopeByWorkspaceIdAndMemberId(workspaceId, memberId);
        if (optionalScope.isEmpty()) {
            return false;
        }
        Scope scope = optionalScope.get();
        return scope.getOrder() <= Scope.USER.getOrder();
    }

}
