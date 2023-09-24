package com.towork.api.global.service;

import com.towork.api.workspace.domain.entity.Scope;
import com.towork.api.workspace.repository.ParticipantRepository;
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
