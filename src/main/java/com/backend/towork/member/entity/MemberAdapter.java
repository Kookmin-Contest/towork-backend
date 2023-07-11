package com.backend.towork.member.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class MemberAdapter extends User {
    private final Member member;

    public MemberAdapter(Member member) {
        super(member.getUsername(), member.getPassword(),
                List.of(new SimpleGrantedAuthority(member.getRole().getRoleType())));
        this.member = member;
    }

    public Member getMember() {
        return this.member;
    }

}
