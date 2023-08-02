package com.backend.towork.member.controller;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.member.domain.dto.request.NameUpdateRequestDto;
import com.backend.towork.member.domain.dto.request.PhoneUpdateRequestDto;
import com.backend.towork.member.domain.dto.response.MemberResponseDto;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.member.service.MemberService;
import com.backend.towork.workspace.domain.dto.response.WorkspaceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "멤버 정보 가져오기",
            description = "토큰에 명시된 멤버 정보를 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 멤버 정보를 가져옴.", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<MemberResponseDto> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principal) {
        MemberResponseDto memberResponseDto = memberService.getMemberInfo(principal.getMember());
        return ResponseEntity.ok()
                .body(memberResponseDto);
    }

    @PatchMapping("/phone")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "멤버 폰 번호 업데이트",
            description = "토큰에 명시된 멤버의 폰 번호를 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 폰 번호를 업데이트함."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> modifyPhone(@Valid @RequestBody PhoneUpdateRequestDto phoneUpdateRequestDto,
                                                       @AuthenticationPrincipal PrincipalDetails principal) {
        memberService.modifyPhone(phoneUpdateRequestDto, principal);
        return ResponseEntity.ok()
                .body(null);
    }

    @PatchMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "멤버 이름 업데이트",
            description = "토큰에 명시된 멤버의 이름을 업데이트합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 이름을 업데이트함."),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> modifyName(@Valid @RequestBody NameUpdateRequestDto nameUpdateRequestDto,
                                                      @AuthenticationPrincipal PrincipalDetails principal) {
        memberService.modifyName(nameUpdateRequestDto, principal);
        return ResponseEntity.ok()
                .body(null);
    }

    @GetMapping("/workspaces")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<WorkspaceResponseDto>> getWorkspacesOfMember(@AuthenticationPrincipal PrincipalDetails principal) {
        List<WorkspaceResponseDto> workspaceResponseDtos = memberService.getWorkspacesOfMember(principal);
        return ResponseEntity.ok()
                .body(workspaceResponseDtos);
    }

}
