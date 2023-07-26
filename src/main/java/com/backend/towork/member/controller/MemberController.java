package com.backend.towork.member.controller;

import com.backend.towork.member.domain.dto.request.NameUpdateRequest;
import com.backend.towork.member.domain.dto.request.PhoneUpdateRequest;
import com.backend.towork.member.domain.dto.response.MemberResponse;
import com.backend.towork.member.domain.entity.PrincipalDetails;
import com.backend.towork.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principal) {
        MemberResponse memberResponse = memberService.getMemberInfo(principal.getMember());
        return ResponseEntity.ok()
                .body(memberResponse);
    }

    @PatchMapping("/phone")
    public ResponseEntity<?> modifyPhone(@Valid @RequestBody PhoneUpdateRequest phoneUpdateRequest,
                                                       @AuthenticationPrincipal PrincipalDetails principal) {
        memberService.modifyPhone(phoneUpdateRequest, principal);
        return ResponseEntity.ok()
                .body(null);
    }

    @PatchMapping("/name")
    public ResponseEntity<?> modifyName(@Valid @RequestBody NameUpdateRequest nameUpdateRequest,
                                                      @AuthenticationPrincipal PrincipalDetails principal) {
        memberService.modifyName(nameUpdateRequest, principal);
        return ResponseEntity.ok()
                .body(null);
    }

}
