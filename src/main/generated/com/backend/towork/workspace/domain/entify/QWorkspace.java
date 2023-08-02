package com.backend.towork.workspace.domain.entify;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWorkspace is a Querydsl query type for Workspace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWorkspace extends EntityPathBase<Workspace> {

    private static final long serialVersionUID = 128882702L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWorkspace workspace = new QWorkspace("workspace");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final com.backend.towork.member.domain.entity.QMember owner;

    public final ListPath<Participant, QParticipant> participants = this.<Participant, QParticipant>createList("participants", Participant.class, QParticipant.class, PathInits.DIRECT2);

    public QWorkspace(String variable) {
        this(Workspace.class, forVariable(variable), INITS);
    }

    public QWorkspace(Path<? extends Workspace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWorkspace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWorkspace(PathMetadata metadata, PathInits inits) {
        this(Workspace.class, metadata, inits);
    }

    public QWorkspace(Class<? extends Workspace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.backend.towork.member.domain.entity.QMember(forProperty("owner")) : null;
    }

}

