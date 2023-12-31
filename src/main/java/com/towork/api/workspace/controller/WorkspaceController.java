package com.towork.api.workspace.controller;

import com.towork.api.global.error.ErrorResponse;
import com.towork.api.member.domain.entity.Member;
import com.towork.api.auth.domain.entity.PrincipalDetails;
import com.towork.api.workspace.domain.dto.request.WorkspaceRequestDto;
import com.towork.api.workspace.domain.dto.response.WorkspaceResponseDto;
import com.towork.api.workspace.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "워크스페이스 생성",
            description = "토큰에 명시된 멤버의 소유로 워크스페이스를 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "성공적으로 워크스페이스를 생성함."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> createWorkspace(@Valid @RequestBody WorkspaceRequestDto workspaceRequestDto,
                                             @AuthenticationPrincipal PrincipalDetails principal) {
        Member member = principal.getMember();
        workspaceService.createWorkspace(workspaceRequestDto, member);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(null);
    }

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "워크스페이스 정보 가져오기",
            description = "주어진 workspaceId에 해당하는 워크스페이스의 정보를 가져옵니다.",
            parameters = {
                    @Parameter(name = "워크스페이스 Ids", description = "정보를 얻고싶은 워크스페이스 id를 ,로 구분하여 전송.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 워크스페이스의 정보를 가져옴."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "주어진 토큰의 멤버가 해당 워크스페이스에 참여자가 아님.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<WorkspaceResponseDto>> getWorkspace(@RequestParam List<Long> workspaceIds) {
        List<WorkspaceResponseDto> workspaceResponseDtoList = workspaceService.getWorkspaceInfoByWorkspaceId(workspaceIds);
        return ResponseEntity.ok()
                .body(workspaceResponseDtoList);
    }
}
