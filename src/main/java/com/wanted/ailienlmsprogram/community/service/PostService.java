package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.dto.PostCreateRequestDTO;
import com.wanted.ailienlmsprogram.community.dto.PostDTO;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.global.filtering.BadWordCheck;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public List<PostDTO> findPostsByContinent(Long continentId) {
        // 리포지토리의 @Query 메서드 호출 (공지 상단 정렬 적용됨)
        return postRepository.findActivePostsByContinent(continentId)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    public PostDTO findPostById(Long postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
        return new PostDTO(post);
    }

    @Transactional
    @BadWordCheck
    public void savePost(PostCreateRequestDTO request, Member member) {
        CommunityPost post = CommunityPost.create(
                request.getContinentId(),
                request.getPostTitle(),
                request.getPostContent(),
                member,
                false
        );

        postRepository.save(post);
    }

    @Transactional
    @BadWordCheck
    public void updatePost(Long postId, PostCreateRequestDTO request) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        post.setPostTitle(request.getPostTitle());
        post.setPostContent(request.getPostContent());
    }

    @Transactional
    public void deletePost(Long postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        post.setPostIsDeleted(true);
    }
}