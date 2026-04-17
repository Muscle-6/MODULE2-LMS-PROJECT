package com.wanted.ailienlmsprogram.community.dto;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.format.DateTimeFormatter; // 추가
 // 추가

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private Long postId;
    private Long continentId;
    private String title;
    private String content;
    private String authorName;
    private String memberId;
    private boolean postIsNotice;
    // 작성일시 필드 추가
    private String createdAt;

    public PostDTO(CommunityPost post) {
        this.postId = post.getPostId();
        this.continentId = post.getContinentId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.postIsNotice = post.isPostIsNotice();

        // 엔티티의 LocalDateTime을 "yyyy-MM-dd" 형식의 문자열로 변환
        if (post.getCreatedAt() != null) {
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        if (post.getMember() != null) {
            this.authorName = post.getMember().getName();
            this.memberId = String.valueOf(post.getMember().getMemberId());
        }
    }
}