package com.backend.towork.member.controller;

import com.backend.towork.global.dto.ResponseDto;
import com.backend.towork.member.dto.MemberDto;
import com.backend.towork.member.entity.Member;
import com.backend.towork.member.entity.Role;
import com.backend.towork.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<?>> join(@RequestBody @Valid MemberDto memberDto, BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto<MemberDto> responseDto = ResponseDto.<MemberDto>builder()
                    .resultCode(404)
                    .message("unexpected error")
                    .data(memberDto)
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(memberDto.getPassword())
                .role(Role.ROLE_USER)
                .build();

        log.info(member.getEmail());
        memberService.join(member);
        ResponseDto<Member> responseDto = ResponseDto.<Member>builder()
                .resultCode(200)
                .message("회원가입 성공")
                .data(member)
                .build();

        return ResponseEntity.ok().body(responseDto);
    }

//    @PostMapping("/login")


}
