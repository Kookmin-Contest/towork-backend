package com.backend.towork.member.repository;

import com.backend.towork.member.entity.Member;

import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {

    void save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();

}
