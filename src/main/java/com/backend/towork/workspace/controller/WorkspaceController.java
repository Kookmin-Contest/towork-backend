package com.backend.towork.workspace.controller;

import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.workspace.domain.dto.request.WorkspaceRequestDto;
import com.backend.towork.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createWorkspace(@Valid @RequestBody WorkspaceRequestDto workspaceRequestDto,
                                             @AuthenticationPrincipal PrincipalDetails principal) {
        Member member = principal.getMember();
        workspaceService.createWorkspace(member.getId(), workspaceRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(null);
    }
}
