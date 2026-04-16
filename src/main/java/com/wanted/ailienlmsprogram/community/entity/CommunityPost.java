package com.wanted.ailienlmsprogram.community.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "continent_post")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CommunityPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private Long continentId;
    private String postTitle;
    private String postContent;
    private boolean postIsDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityComment> comments = new ArrayList<>();

    // CommunityPost.java 엔티티 파일 안의 적당한 곳(필드 아래쪽)에 붙여넣으세요.

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setPostIsDeleted(boolean postIsDeleted) {
        this.postIsDeleted = postIsDeleted;
    }
}