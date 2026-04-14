package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.dto.PostDTO;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
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
}