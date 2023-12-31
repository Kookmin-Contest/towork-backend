package com.towork.api.member.controller;

import com.towork.api.global.error.ErrorResponse;
import com.towork.api.member.domain.dto.request.NameUpdateRequestDto;
import com.towork.api.member.domain.dto.request.PhoneUpdateRequestDto;
import com.towork.api.member.domain.dto.response.MemberResponseDto;
import com.towork.api.member.domain.entity.Member;
import com.towork.api.auth.domain.entity.PrincipalDetails;
import com.towork.api.member.service.MemberService;
import com.towork.api.workspace.domain.dto.response.WorkspaceResponseDto;
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
    public ResponseEntity<MemberResponseDto> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        MemberResponseDto memberResponseDto = memberService.getMemberInfo(member);
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
                                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        memberService.modifyPhone(phoneUpdateRequestDto, member);
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
        Member member = principal.getMember();
        memberService.modifyName(nameUpdateRequestDto, member);
        return ResponseEntity.ok()
                .body(null);
    }

    @GetMapping("/workspaces")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "멤버 워크스페이스 정보 가져오기",
            description = "토큰의 명시된 멤버의 모든 워크스페이스의 정보를 가져옵니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 모든 워크스페이스의 정보를 가져옴."),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<List<WorkspaceResponseDto>> getWorkspacesOfMember(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        List<WorkspaceResponseDto> workspaceResponseDtos = memberService.getWorkspacesOfMember(member);
        return ResponseEntity.ok()
                .body(workspaceResponseDtos);
    }

}
