package com.towork.api.workspace.repository;

import com.towork.api.workspace.domain.entity.Scope;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.towork.api.workspace.domain.entity.QParticipant.participant;

@Repository
@RequiredArgsConstructor
public class ParticipantRepository {
    
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    
    public Optional<Scope> getScopeByWorkspaceIdAndMemberId(Long workspaceId, Long memberId) {
        Scope scope = queryFactory.from(participant)
                .select(participant.scope)
                .where(participant.workspace.id.eq(workspaceId))
                .where(participant.member.id.eq(memberId))
                .fetchOne();
        return Optional.ofNullable(scope);
    }
}
