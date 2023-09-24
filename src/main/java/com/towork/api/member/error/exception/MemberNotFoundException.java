package com.towork.api.member.error.exception;

import com.towork.api.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException() {
        super("해당 멤버는 존재하지 않습니다.");
    }

}
