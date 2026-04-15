package com.wanted.ailienlmsprogram.community.controller;

import com.wanted.ailienlmsprogram.community.dto.PostCreateRequest;
import com.wanted.ailienlmsprogram.community.dto.PostDTO;
import com.wanted.ailienlmsprogram.community.service.PostService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 목록 페이지 이동
    @GetMapping("/continents/{continentId}/posts")
    public String findPostListByContinent(@PathVariable Long continentId, Model model) {
        List<PostDTO> posts = postService.findPostsByContinent(continentId);
        model.addAttribute("posts", posts);
        model.addAttribute("continentId", continentId);
        return "community/posts";
    }

    // 상세 페이지 이동
    @GetMapping("/posts/{postId}")
    public String findPostDetail(@PathVariable Long postId,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostDTO post = postService.findPostById(postId);
        model.addAttribute("post", post);

        // 현재 로그인 유저 정보가 있다면 String으로 변환해서 화면에 전달
        if (userDetails != null) {
            String currentUserId = String.valueOf(userDetails.getMember().getMemberId());
            model.addAttribute("currentUserId", currentUserId);
        }

        return "community/post_detail";
    }

    // 1. 새 글 쓰기 화면 이동
    @GetMapping("/continents/{continentId}/posts/new")
    public String createPostForm(@PathVariable Long continentId, Model model) {
        model.addAttribute("continentId", continentId);
        model.addAttribute("postCreateRequest", new PostCreateRequest());
        return "community/post_create";
    }

    // 2. 새 글 저장 처리
    @PostMapping("/continents/{continentId}/posts/new")
    public String createPost(@PathVariable Long continentId,
                             @ModelAttribute PostCreateRequest request,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member loginMember = userDetails.getMember();
        request.setContinentId(continentId);
        postService.savePost(request, loginMember);

        return "redirect:/continents/" + continentId + "/posts";
    }
}