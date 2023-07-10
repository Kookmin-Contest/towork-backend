package com.backend.towork.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}