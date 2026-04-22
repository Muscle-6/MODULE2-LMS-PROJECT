package com.wanted.ailienlmsprogram.community.dto;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.member.entity.Member;
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
    private String continentName;
    private String title;
    private String content;
    private String authorName;
    private String memberId;
    private boolean postIsNotice;
    private String createdAt;


    private String authorRank;
    private String authorRankComment;

    public PostDTO(CommunityPost post) {
        this.postId = post.getPostId();
        this.continentId = post.getContinentId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();
        this.postIsNotice = post.isPostIsNotice();

        // 대륙 번호를 이름으로 변환
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

        // 작성자 정보 및 등급/멘트 매핑
        if (post.getMember() != null) {
            Member member = post.getMember();
            this.authorName = member.getName();
            this.memberId = String.valueOf(member.getMemberId());

            // 멤버 엔티티의 Rank 정보를 DTO 필드에 담아줍니다.
            if (member.getRank() != null) {
                // 1. 영문 등급명 저장 (HTML의 조건문에서 사용)
                this.authorRank = member.getRank().name();

                // 2. 한글 멘트 매핑 (화면에 출력)
                switch (member.getRank()) {
                    case REPTILIAN:
                        this.authorRankComment = "왕족 렙틸리언";
                        break;
                    case MINERVAL:
                        this.authorRankComment = "곧 노비스입니다";
                        break;
                    case NOVICE:
                        this.authorRankComment = "저는 게으른 노비스입니다";
                        break;
                }
            }
        }
    }
}