package com.towork.api.member.domain.entity;

import com.towork.api.workspace.domain.entity.Participant;
import com.towork.api.workspace.domain.entity.Workspace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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

    @Builder.Default
    @OneToMany(mappedBy = "owner")
    List<Workspace> workspaces = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member")
    List<Participant> participants = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public int getCountWorkspace() {
        return workspaces.size();
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeName(String name) {
        this.name = name;
    }
}