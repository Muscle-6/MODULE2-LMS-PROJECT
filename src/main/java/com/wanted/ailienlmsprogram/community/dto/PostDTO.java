package com.wanted.ailienlmsprogram.community.dto;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost; // 이 경로가 정답일 거예요!
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


    // [정석] 엔티티를 받아서 DTO로 변환
    public PostDTO(CommunityPost post) {
        this.postId = post.getPostId();
        this.continentId = post.getContinentId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();

        // 멤버 정보가 있다면 이름을 가져옵니다.
        if (post.getMember() != null) {
            this.authorName = post.getMember().getName();

            // String.valueOf()로 감싸서 숫자를 문자로 바꿔줍니다.
            this.memberId = String.valueOf(post.getMember().getMemberId());
        }   }
}