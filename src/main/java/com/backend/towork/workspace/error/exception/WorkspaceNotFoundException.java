package com.backend.towork.workspace.error.exception;

import com.backend.towork.global.error.exception.EntityNotFoundException;

public class WorkspaceNotFoundException extends EntityNotFoundException {

    public WorkspaceNotFoundException(Long workspaceId) {
        super("workspaceId: " + workspaceId + " -> 해당 워크스페이스는 존재하지 않습니다.");
    }
}
