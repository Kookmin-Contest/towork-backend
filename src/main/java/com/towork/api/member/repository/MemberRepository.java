package com.towork.api.member.repository;

import com.towork.api.member.domain.entity.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    void save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);

}