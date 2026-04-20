package com.wanted.ailienlmsprogram.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
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
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "login_id", nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;


    @Column(name = "member_name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", nullable = false)
    private AccountStatus accountStatus;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_rank")
    private MemberRank rank;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    public void upgradeRank() {
        if (this.rank == MemberRank.NOVICE) {
            this.rank = MemberRank.MINERVAL;
        } else if (this.rank == MemberRank.MINERVAL) {
            this.rank = MemberRank.REPTILIAN;
        }
    }

    public void updateLastLogin(LocalDateTime now) {
        this.lastLoginAt = now;
        this.updatedAt = now;
    }

    public void demoteRank(MemberRank rank) {
        this.rank = rank;
    }

    // 생성 메서드
    public static Member create(String loginId, String email, String password, String name,
                                String phone, MemberRole role, AccountStatus status, MemberRank rank) {
        Member member = new Member();
        member.loginId = loginId;
        member.email = email;
        member.password = password;
        member.name = name;
        member.phone = phone;
        member.role = role;
        member.accountStatus = status;
        member.rank = rank;
        member.createdAt = LocalDateTime.now();
        member.updatedAt = LocalDateTime.now();
        return member;
    }
}