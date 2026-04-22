package com.wanted.ailienlmsprogram.community.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continent_post")
@Getter
@NoArgsConstructor
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "continent_id")
    private Long continentId;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_content", columnDefinition = "TEXT")
    private String postContent;

    @Column(name = "post_is_deleted")
    private boolean postIsDeleted;

    @Column(name = "post_is_notice", nullable = false)
    private boolean postIsNotice = false;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = java.time.LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();

    // 도메인 메서드
    public void updateContent(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public void delete() {
        this.postIsDeleted = true;
    }

    // 생성 메서드
    public static CommunityPost create(Long continentId, String title, String content, Member member, boolean isNotice) {
        CommunityPost post = new CommunityPost();
        post.continentId = continentId;
        post.postTitle = title;
        post.postContent = content;
        post.member = member;
        post.postIsNotice = isNotice;
        post.postIsDeleted = false;
        return post;
    }
}