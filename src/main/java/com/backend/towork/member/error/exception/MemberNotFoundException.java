package com.backend.towork.member.error.exception;

import com.backend.towork.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException() {
        super("해당 멤버는 존재하지 않습니다.");
    }

}
