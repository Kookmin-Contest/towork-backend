package com.backend.towork.member.domain.entity;

import com.backend.towork.oauth.provider.OauthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String email;

    @JsonIgnore
    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String authProvider;

    @Setter
    private LocalDate birthDate;

    @Setter
    private String phoneNumber;

    @NotNull
    @CreatedDate
    private LocalDateTime createDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeName(String name) {
        this.name = name;
    }
}