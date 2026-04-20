package com.wanted.ailienlmsprogram.community.dto;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private Long postId;
    private Long continentId;
    private String continentName; // 대륙 이름 필드
    private String title;
    private String content;
    private String authorName;
    private String memberId;
    private boolean postIsNotice;
    private String createdAt;

    public PostDTO(CommunityPost post) {
        this.postId = post.getPostId();
        this.continentId = post.getContinentId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.postIsNotice = post.isPostIsNotice();

        // ★ 대륙 번호를 이름으로 변환 (프로젝트 설정에 맞게 수정하세요!)
        if (this.continentId != null) {
            switch (this.continentId.intValue()) {
                case 1: this.continentName = "Java Continent"; break;
                case 2: this.continentName = "Spring Continent"; break;
                default: this.continentName = "Database Continent"; break;
            }
        }

        // 날짜 형식 변환
        if (post.getCreatedAt() != null) {
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        // 작성자 정보 매핑
        if (post.getMember() != null) {
            this.authorName = post.getMember().getName();
            this.memberId = String.valueOf(post.getMember().getMemberId());
        }
    }
}