package com.backend.towork.member.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1435398036L;

    public static final QMember member = new QMember("member1");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> createDateTime = createDateTime("createDateTime", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<com.backend.towork.workspace.domain.entity.Participant, com.backend.towork.workspace.domain.entity.QParticipant> participants = this.<com.backend.towork.workspace.domain.entity.Participant, com.backend.towork.workspace.domain.entity.QParticipant>createList("participants", com.backend.towork.workspace.domain.entity.Participant.class, com.backend.towork.workspace.domain.entity.QParticipant.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final ListPath<com.backend.towork.workspace.domain.entity.Workspace, com.backend.towork.workspace.domain.entity.QWorkspace> workspaces = this.<com.backend.towork.workspace.domain.entity.Workspace, com.backend.towork.workspace.domain.entity.QWorkspace>createList("workspaces", com.backend.towork.workspace.domain.entity.Workspace.class, com.backend.towork.workspace.domain.entity.QWorkspace.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

