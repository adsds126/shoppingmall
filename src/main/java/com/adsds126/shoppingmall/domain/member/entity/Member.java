package com.adsds126.shoppingmall.domain.member.entity;

import com.adsds126.shoppingmall.audit.Auditable;
import com.adsds126.shoppingmall.domain.member.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MEMBERS")
public class Member extends Auditable {

    @Id
    @Column(name = "MEMBER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128, nullable = false)
    @Size(max = 128)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    @Email
    private String email;

    @Column(name = "ROLE_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    private Boolean emailVerifiedYn;

    @Column(name = "MODIFIED_AT", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}
