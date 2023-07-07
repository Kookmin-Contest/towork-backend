package com.backend.towork.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;


@Entity
@Builder
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createDateTime;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

}