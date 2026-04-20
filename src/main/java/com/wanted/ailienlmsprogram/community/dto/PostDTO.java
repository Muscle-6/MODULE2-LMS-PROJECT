package com.wanted.ailienlmsprogram.community.dto;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.member.entity.Member; // Member 엔티티 참조
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

    // --- 신규 추가 필드 ---
    private String authorRank;     // Enum 값 (REPTILIAN, MINERVAL, NOVICE)
    private String authorRankMent; // 실제 화면에 보일 말풍선 멘트
    // ---------------------

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

        // 작성자 정보 및 랭크 정보 매핑
        if (post.getMember() != null) {
            Member author = post.getMember();
            this.authorName = author.getName();
            this.memberId = String.valueOf(author.getMemberId());

            // ★ 팀장님 가이드: Member 엔티티의 rank를 가져와서 멘트로 변환
            if (author.getRank() != null) {
                this.authorRank = author.getRank().name(); // Enum 이름을 String으로 저장

                // 정책서에 따른 멘트 세팅
                switch (author.getRank()) {
                    case REPTILIAN:
                        this.authorRankMent = "왕족 렙틸리언";
                        break;
                    case MINERVAL:
                        this.authorRankMent = "곧 노비스입니다";
                        break;
                    case NOVICE:
                        this.authorRankMent = "저는 게으른 노비스입니다";
                        break;
                    default:
                        this.authorRankMent = "";
                        break;
                }
            }
        }
    }
}