package com.backend.towork.workspace.controller;

import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.workspace.domain.dto.request.WorkspaceRequestDto;
import com.backend.towork.workspace.domain.dto.response.WorkspaceResponseDto;
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
        workspaceService.createWorkspace(member, workspaceRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(null);
    }

    @GetMapping("/{workspaceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<WorkspaceResponseDto> getWorkspace(@PathVariable Long workspaceId,
                                                             @AuthenticationPrincipal PrincipalDetails principal) {
        Member member = principal.getMember();
        WorkspaceResponseDto responseDto = workspaceService.getWorkspace(workspaceId, member);
        return ResponseEntity.ok()
                .body(responseDto);
    }
}
