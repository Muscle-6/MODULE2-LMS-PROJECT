package com.wanted.ailienlmsprogram.admin.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "continent_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminContinentNoticePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private Long continentId;
    private String postTitle;
    private String postContent;
    private boolean postIsDeleted;
    private boolean postIsNotice;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    private AdminContinentNoticePost(Long continentId, Member member, String postTitle, String postContent) {
        this.continentId = continentId;
        this.member = member;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postIsDeleted = false;
        this.postIsNotice = true;
    }

    public static AdminContinentNoticePost createNotice(Long continentId, Member member, String title, String content) {
        return new AdminContinentNoticePost(continentId, member, title, content);
    }
}