package com.wanted.ailienlmsprogram.community.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "continent_comment") // 1. 테이블 이름 연결
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "comment_content", columnDefinition = "TEXT", nullable = false) // 2. 컬럼 이름 연결
    private String content;

    @Column(name = "comment_is_deleted")
    private boolean isDeleted = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private CommunityPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id") // 3. 작성자 ID 컬럼 연결
    private Member member;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public static CommunityComment create(String content, CommunityPost post, Member member) {
        CommunityComment comment = new CommunityComment();
        comment.content = content;
        comment.post = post;
        comment.member = member;
        return comment;
    }
}