package com.backend.towork.workspace.repository;

import com.backend.towork.workspace.domain.entify.Scope;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.backend.towork.workspace.domain.entify.QParticipant.participant;

@Repository
@RequiredArgsConstructor
public class ParticipantRepository {
    
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    
    public Optional<Scope> getRoleByWorkspaceIdAndMemberId(Long workspaceId, Long memberId) {
        Scope scope = queryFactory.from(participant)
                .select(participant.scope)
                .where(participant.workspace.id.eq(workspaceId))
                .where(participant.member.id.eq(memberId))
                .fetchOne();
        return Optional.ofNullable(scope);
    }
}
