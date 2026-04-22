package com.wanted.ailienlmsprogram.community.controller;

import com.wanted.ailienlmsprogram.community.dto.CommentRequestDTO; // 1. DTO 임포트 확인
import com.wanted.ailienlmsprogram.community.service.CommentService;
import com.wanted.ailienlmsprogram.community.service.PostService;
import com.wanted.ailienlmsprogram.global.filtering.BadWordDetectedException;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 등록 (DTO 방식으로 수정)
    @PostMapping
    public String createComment(@PathVariable Long postId,
                                @ModelAttribute CommentRequestDTO request, // 2. @RequestParam String content 대신 DTO로 받기
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                Model model) {

        try {
            // 3. 서비스에 DTO(request)를 통째로 넘깁니다. (타입 불일치 해결!)
            commentService.saveComment(postId, request, userDetails.getMember());
            return "redirect:/posts/" + postId;

        } catch (BadWordDetectedException e) {
            // 욕설 감지 시 데이터를 다시 담아 상세 페이지로!
            model.addAttribute("post", postService.findPostById(postId));
            model.addAttribute("comments", commentService.findComments(postId));
            model.addAttribute("errorMessage", e.getMessage());

            // 4. 작성 중이던 댓글 내용을 HTML로 다시 보내기 (데이터 유지의 핵심)
            model.addAttribute("commentRequestDTO", request);

            if (userDetails != null) {
                model.addAttribute("currentUserId", String.valueOf(userDetails.getMember().getMemberId()));
            }

            return "community/post_detail";
        }
    }

    // 댓글 삭제 (기존 동일)
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getMember());
        return "redirect:/posts/" + postId;
    }
}