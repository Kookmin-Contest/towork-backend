package com.backend.towork.global.security;

import com.backend.towork.member.domain.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("auth")
public class AuthorizationLogic {

    public boolean isParticipant(Long workspaceId, Member member) {
        return member.getParticipants().stream()
                .anyMatch(participant -> participant.getWorkspace().getId().equals(workspaceId));
    }

}
