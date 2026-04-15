package com.wanted.ailienlmsprogram.community.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    // 4. "cannot be null" 에러 해결사! 생성 시간 필드
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 5. 저장하기 직전에 시간을 자동으로 넣어주는 메서드
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}