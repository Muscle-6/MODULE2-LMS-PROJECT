package com.wanted.ailienlmsprogram.community.controller;

import com.wanted.ailienlmsprogram.community.service.CommentService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping
    public String createComment(@PathVariable Long postId,
                                @RequestParam String content,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.saveComment(postId, content, userDetails.getMember());
        return "redirect:/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getMember());
        return "redirect:/posts/" + postId;
    }
}