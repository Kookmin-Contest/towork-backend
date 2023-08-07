package com.backend.towork.member.repository;

import com.backend.towork.member.domain.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.backend.towork.member.domain.entity.QMember.member;
import static com.backend.towork.workspace.domain.entity.QParticipant.participant;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Member member) {
        em.persist(member);
    }

    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(
                em.find(Member.class, memberId)
        );
    }

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(
                queryFactory.selectFrom(member)
                        .where(member.email.eq(email))
                        .fetchOne()
        );
    }

    public List<Member> findByWorkspaceId(Long workspaceId) {
        return queryFactory.selectFrom(member)
                .innerJoin(participant)
                .on(member.id.eq(participant.member.id))
                .where(participant.workspace.id.eq(workspaceId))
                .fetch();
    }
}