package com.backend.towork.integration.controller;

import com.backend.towork.TCIntegrationTest;
import com.backend.towork.auth.domain.dto.request.LoginRequestDto;
import com.backend.towork.member.domain.dto.request.MemberRequestDto;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.mapper.MemberMapper;
import com.backend.towork.member.repository.MemberRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
@AutoConfigureMockMvc
public class AuthControllerTest extends TCIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    private final Gson gson = new Gson();
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;

    @Test
    @DisplayName("회원가입_성공")
    void signup_success() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .email("test@gmail.com")
                .password("test")
                .name("홍길동")
                .birthDate("2000-01-01")
                .phoneNumber("010-1234-1234")
                .build();
        String data = gson.toJson(memberRequestDto);

        // then
        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입_실패: 이메일_중복")
    void signup_fail_duplicate_email() throws Exception {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .email("test@gmail.com")
                .password("test")
                .name("홍길동")
                .birthDate("2000-01-01")
                .phoneNumber("010-1234-1234")
                .build();
        Member member = memberMapper.requestDtoToEntity(memberRequestDto);
        String data = gson.toJson(memberRequestDto);

        // when
        memberRepository.save(member);

        // then
        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인_성공")
    void login_success() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        String data = gson.toJson(loginRequestDto);

        // when
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .email("test@gmail.com")
                .password("test")
                .name("홍길동")
                .birthDate("2000-01-01")
                .phoneNumber("010-1234-1234")
                .build();
        Member member = memberMapper.requestDtoToEntity(memberRequestDto);
        memberRepository.save(member);

        // then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인_실패: 없는 아이디 조회")
    void login_fail_no_account() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        String data = gson.toJson(loginRequestDto);

        // then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인_실패: 비밀번호 틀림")
    void login_fail_wrong_password() throws Exception {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("test@gmail.com")
                .password("wrong_password")
                .build();
        String data = gson.toJson(loginRequestDto);

        // when
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .email("test@gmail.com")
                .password("correct_password")
                .name("홍길동")
                .birthDate("2000-01-01")
                .phoneNumber("010-1234-1234")
                .build();
        Member member = memberMapper.requestDtoToEntity(memberRequestDto);
        memberRepository.save(member);

        // then
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
        ).andExpect(status().isBadRequest());
    }
}
