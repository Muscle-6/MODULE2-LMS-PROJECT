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


    public PostDTO(CommunityPost post) {
        this.postId = post.getPostId();
        this.continentId = post.getContinentId();
        this.title = post.getPostTitle();
        this.content = post.getPostContent();

        if (post.getMember() != null) {
            this.authorName = post.getMember().getName();

            this.memberId = String.valueOf(post.getMember().getMemberId());
        }   }
}