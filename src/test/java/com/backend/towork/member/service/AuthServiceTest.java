package com.backend.towork.member.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class AuthServiceTest {

    @Autowired
    AuthService authService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void register() {
    }

}