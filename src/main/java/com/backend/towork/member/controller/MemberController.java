package com.backend.towork.member.controller;

import com.backend.towork.global.domain.dto.response.ErrorResponse;
import com.backend.towork.member.domain.dto.request.NameUpdateRequest;
import com.backend.towork.member.domain.dto.request.PhoneUpdateRequest;
import com.backend.towork.member.domain.dto.response.MemberResponse;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.member.service.MemberService;
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
                    @ApiResponse(responseCode = "200", description = "성공적으로 멤버 정보를 가져옴.", content = @Content(schema = @Schema(implementation = MemberResponse.class))),
                    @ApiResponse(responseCode = "400", description = "주어진 정보가 올바르지 않음.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "만료된 토큰이거나 잘못된 토큰임.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principal) {
        MemberResponse memberResponse = memberService.getMemberInfo(principal.getMember());
        return ResponseEntity.ok()
                .body(memberResponse);
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
    public ResponseEntity<?> modifyPhone(@Valid @RequestBody PhoneUpdateRequest phoneUpdateRequest,
                                                       @AuthenticationPrincipal PrincipalDetails principal) {
        memberService.modifyPhone(phoneUpdateRequest, principal);
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
    public ResponseEntity<?> modifyName(@Valid @RequestBody NameUpdateRequest nameUpdateRequest,
                                                      @AuthenticationPrincipal PrincipalDetails principal) {
        memberService.modifyName(nameUpdateRequest, principal);
        return ResponseEntity.ok()
                .body(null);
    }

}
