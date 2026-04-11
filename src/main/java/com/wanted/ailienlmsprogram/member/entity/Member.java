package com.wanted.ailienlmsprogram.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    public enum MemberRole {
        STUDENT, INSTRUCTOR, ADMIN
    }

    public enum AccountStatus {
        ACTIVE, INACTIVE, BANNED
    }

    public enum MemberRank {
        REPTILIAN, MINERVAL, NOVICE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "회원번호")
    private Long memberId;

    @Column(name = "아이디", nullable = false, unique = true)
    private String loginId;


    @Column(name = "이메일", nullable = false, unique = true)
    private String email;

    @Column(name = "비밀번호", nullable = false)
    private String password;

    @Column(name = "이름", nullable = false)
    private String name;

    @Column(name = "전화번호")
    private String phone;

    @Column(name = "프로필이미지URL")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "역할", nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "계정상태", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "자기소개")
    private String introduction;

    @Column(name = "가입일시")
    private LocalDateTime createdAt;

    @Column(name = "수정일시")
    private LocalDateTime updatedAt;

    @Column(name = "탈퇴일시")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "등급")
    private MemberRank rank;

    @Column(name = "마지막로그인일시")
    private LocalDateTime lastLoginAt;
}