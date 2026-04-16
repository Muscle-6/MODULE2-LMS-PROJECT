package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.dto.PostCreateRequest;
import com.wanted.ailienlmsprogram.community.dto.PostDTO;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
        return postRepository.findByContinentIdAndPostIsDeletedFalse(continentId)
                .stream()
                .map(PostDTO::new)
                .collect(Collectors.toList());
    }

    public PostDTO findPostById(Long postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        return new PostDTO(post);
    }

    // 1. 저장 로직 추가 (수정/삭제가 가능하도록 별도의 @Transactional 추가)
    @Transactional
    public void savePost(PostCreateRequest request, Member member) {
        // DTO를 엔티티로 변환 (Builder 패턴 사용)
        CommunityPost post = CommunityPost.builder()
                .postTitle(request.getPostTitle())
                .postContent(request.getPostContent())
                .continentId(request.getContinentId())
                .member(member) // 작성자(Member) 객체 연결
                .postIsDeleted(false) // 기본값 설정
                .build();

        postRepository.save(post);
    }

    // 2. 수정 로직 추가
    @Transactional
    public void updatePost(Long postId, PostCreateRequest request) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        // 엔티티의 필드를 직접 수정 (Dirty Checking으로 자동 반영됨)
        post.setPostTitle(request.getPostTitle());
        post.setPostContent(request.getPostContent());
    }

    // 3. 삭제 로직 추가 (소프트 딜리트)
    @Transactional
    public void deletePost(Long postId) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        post.setPostIsDeleted(true); // 삭제 여부만 true로 변경
    }
}