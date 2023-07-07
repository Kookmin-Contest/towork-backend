package com.backend.towork.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.towork.user.entity.User;
import com.backend.towork.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;

    @Transactional
    public Long join(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("동일한 이메일을 가진 아이디가 있습니다.");
        }
        log.info("abc");
        userRepository.save(user);
        return user.getId();
    }
}
