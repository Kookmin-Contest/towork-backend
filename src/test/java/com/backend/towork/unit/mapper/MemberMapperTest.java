package com.backend.towork.unit.mapper;

import com.backend.towork.member.domain.dto.request.MemberRequestDto;
import com.backend.towork.member.domain.entity.Member;
import com.backend.towork.member.domain.mapper.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.shaded.org.bouncycastle.openssl.PasswordException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MemberMapperTest {

    private final MemberMapper mapper = MemberMapper.INSTANCE;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("MemberRequestDto -> MemberEntity")
    void memberRequestDtoToMemberEntity() {
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .email("test@gmail.com")
                .password("12345678")
                .name("홍길동")
                .birthDate("2000-01-01")
                .phoneNumber("010-1234-1234")
                .build();

        Member member = mapper.requestDtoToEntity(memberRequestDto);

        assertNotNull(member);
        assertEquals(memberRequestDto.getEmail(), member.getEmail());
        assertTrue(passwordEncoder.matches(memberRequestDto.getPassword(), member.getPassword()));
        assertEquals(memberRequestDto.getName(), member.getName());
        assertEquals(memberRequestDto.getPhoneNumber(), member.getPhoneNumber());
        assertEquals(LocalDate.parse(memberRequestDto.getBirthDate()), member.getBirthDate());
    }

}
