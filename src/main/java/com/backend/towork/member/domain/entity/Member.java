package com.backend.towork.member.domain.entity;

import com.backend.towork.member.domain.dto.response.MemberResponseDto;
import com.backend.towork.workspace.domain.entity.Participant;
import com.backend.towork.workspace.domain.entity.Workspace;
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

    @OneToMany(mappedBy = "owner")
    List<Workspace> workspaces = new ArrayList<>();

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

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .memberId(this.id)
                .email(this.email)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .birthDate(this.birthDate)
                .createDateTime(this.createDateTime)
                .countWorkspace(this.getCountWorkspace())
                .build();
    }
}