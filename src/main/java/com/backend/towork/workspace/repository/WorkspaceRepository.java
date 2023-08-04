package com.backend.towork.workspace.repository;

import com.backend.towork.workspace.domain.entity.Workspace;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Workspace workspace) {
        em.persist(workspace);
    }

    public Optional<Workspace> findById(Long id) {
        Workspace workspace = em.find(Workspace.class, id);
        return Optional.ofNullable(workspace);
    }
}
