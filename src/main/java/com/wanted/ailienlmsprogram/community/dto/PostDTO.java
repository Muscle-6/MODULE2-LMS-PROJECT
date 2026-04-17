package com.wanted.ailienlmsprogram.community.dto;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    // 1. 공지사항 여부 필드 추가
    private boolean postIsNotice;

    public PostDTO(CommunityPost post) {
        this.postId = post.getPostId();
        this.continentId = post.getContinentId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();

        // 2. 엔티티에서 데이터를 꺼내와서 DTO에 저장 (매우 중요!)
        this.postIsNotice = post.isPostIsNotice();

        if (post.getMember() != null) {
            this.authorName = post.getMember().getName();
            this.memberId = String.valueOf(post.getMember().getMemberId());
        }
    }
}