package com.backend.towork.member.controller;

import com.backend.towork.global.domain.dto.response.DataResponse;
import com.backend.towork.member.domain.dto.response.MemberResponse;
import com.backend.towork.member.domain.entity.PrincipleDetails;
import com.backend.towork.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<DataResponse<MemberResponse>> getMemberInfo(@AuthenticationPrincipal PrincipleDetails principle) {
        MemberResponse memberResponse = memberService.getMemberInfo(principle.getMember());
        return ResponseEntity.ok()
                .body(new DataResponse<>(memberResponse));
    }
}
