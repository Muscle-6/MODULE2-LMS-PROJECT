package com.wanted.ailienlmsprogram.community.controller;

import com.wanted.ailienlmsprogram.community.dto.CommentRequestDTO; // 1. 댓글 요청 DTO 임포트 추가
import com.wanted.ailienlmsprogram.community.dto.PostCreateRequestDTO;
import com.wanted.ailienlmsprogram.community.dto.PostDTO;
import com.wanted.ailienlmsprogram.community.dto.CommentResponseDTO;
import com.wanted.ailienlmsprogram.community.service.PostService;
import com.wanted.ailienlmsprogram.community.service.CommentService;
import com.wanted.ailienlmsprogram.global.filtering.BadWordDetectedException;
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
    private final CommentService commentService;

    // 목록 페이지 이동
    @GetMapping("/continents/{continentId}/posts")
    public String findPostListByContinent(@PathVariable Long continentId, Model model) {
        List<PostDTO> posts = postService.findPostsByContinent(continentId);
        model.addAttribute("posts", posts);
        model.addAttribute("continentId", continentId);
        return "community/posts";
    }

    // 상세 페이지 이동 (수정됨)
    @GetMapping("/posts/{postId}")
    public String findPostDetail(@PathVariable Long postId,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        PostDTO post = postService.findPostById(postId);
        model.addAttribute("post", post);

        List<CommentResponseDTO> comments = commentService.findComments(postId);
        model.addAttribute("comments", comments);

        // ★ 핵심: 상세 페이지를 처음 열 때 빈 댓글 DTO를 모델에 담아줍니다.
        // 이래야 html의 th:object="${commentRequestDTO}" 가 에러 없이 작동합니다.
        model.addAttribute("commentRequestDTO", new CommentRequestDTO());

        if (userDetails != null) {
            String currentUserId = String.valueOf(userDetails.getMember().getMemberId());
            model.addAttribute("currentUserId", currentUserId);
        }

        return "community/post_detail";
    }

    // 새 글 쓰기 화면 이동
    @GetMapping("/continents/{continentId}/posts/new")
    public String createPostForm(@PathVariable Long continentId, Model model) {
        model.addAttribute("continentId", continentId);
        model.addAttribute("postCreateRequest", new PostCreateRequestDTO());
        return "community/post_create";
    }

    // 새 글 저장 처리
    @PostMapping("/continents/{continentId}/posts/new")
    public String createPost(@PathVariable Long continentId,
                             @ModelAttribute PostCreateRequestDTO request,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             Model model) {

        try {
            Member loginMember = userDetails.getMember();
            request.setContinentId(continentId);
            postService.savePost(request, loginMember);

            return "redirect:/continents/" + continentId + "/posts";

        } catch (BadWordDetectedException e) {
            model.addAttribute("continentId", continentId);
            model.addAttribute("postCreateRequest", request);
            model.addAttribute("errorMessage", e.getMessage());
            return "community/post_create";
        }
    }

    // 수정 화면 이동
    @GetMapping("/continents/posts/edit/{postId}")
    public String updatePostForm(@PathVariable Long postId, Model model) {
        PostDTO post = postService.findPostById(postId);
        model.addAttribute("post", post);
        return "community/post_update";
    }

    // 수정 처리
    @PostMapping("/continents/posts/edit/{postId}")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostCreateRequestDTO request,
                             Model model) {
        try {
            postService.updatePost(postId, request);
            return "redirect:/posts/" + postId;

        } catch (BadWordDetectedException e) {
            model.addAttribute("postId", postId);
            model.addAttribute("post", request);
            model.addAttribute("errorMessage", e.getMessage());
            return "community/post_update";
        }
    }

    // 삭제 처리
    @PostMapping("/posts/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        PostDTO post = postService.findPostById(postId);
        Long continentId = post.getContinentId();

        postService.deletePost(postId);
        return "redirect:/continents/" + continentId + "/posts";
    }
}