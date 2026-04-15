package com.wanted.ailienlmsprogram.community.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}