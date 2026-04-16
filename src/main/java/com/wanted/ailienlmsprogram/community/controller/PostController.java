package com.wanted.ailienlmsprogram.community.controller;

import com.wanted.ailienlmsprogram.community.dto.PostCreateRequest;
import com.wanted.ailienlmsprogram.community.dto.PostDTO;
import com.wanted.ailienlmsprogram.community.dto.CommentResponse;
import com.wanted.ailienlmsprogram.community.service.PostService;
import com.wanted.ailienlmsprogram.community.service.CommentService;
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
    private final CommentService commentService; // 댓글 서비스 주입 추가

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

        // 1. 게시글 정보 가져오기
        PostDTO post = postService.findPostById(postId);
        model.addAttribute("post", post);

        // 2. 댓글 목록 가져와서 모델에 담기★
        // 서비스의 findComments 메서드를 호출해서 'comments'라는 이름으로 넘김
        List<CommentResponse> comments = commentService.findComments(postId);
        model.addAttribute("comments", comments);

        // 현재 로그인 유저 정보 처리
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
        model.addAttribute("postCreateRequest", new PostCreateRequest());
        return "community/post_create";
    }

    // 새 글 저장 처리
    @PostMapping("/continents/{continentId}/posts/new")
    public String createPost(@PathVariable Long continentId,
                             @ModelAttribute PostCreateRequest request,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        Member loginMember = userDetails.getMember();
        request.setContinentId(continentId);
        postService.savePost(request, loginMember);

        return "redirect:/continents/" + continentId + "/posts";
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
                             @ModelAttribute PostCreateRequest request) {
        postService.updatePost(postId, request);
        return "redirect:/posts/" + postId;
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