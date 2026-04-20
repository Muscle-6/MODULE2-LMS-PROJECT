package com.wanted.ailienlmsprogram.community.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continent_post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private Long continentId;
    private String postTitle;
    private String postContent;
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

    public void setPostIsNotice(boolean postIsNotice) {
        this.postIsNotice = postIsNotice;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setPostIsDeleted(boolean postIsDeleted) {
        this.postIsDeleted = postIsDeleted;
    }

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