package com.backend.towork.workspace.domain.entity;

import lombok.Getter;

@Getter
public enum Scope {
    OWNER("OWNER", 1),
    MANAGER("MANAGER", 2),
    USER("USER", 3);

    final String scope;
    final int order;

    Scope(String scope, int order) {
        this.scope = scope;
        this.order = order;
    }
}
