package com.backend.towork.member.repository;

import com.backend.towork.member.domain.entity.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    void save(Member member);
    Optional<Member> findByEmail(String email);

}