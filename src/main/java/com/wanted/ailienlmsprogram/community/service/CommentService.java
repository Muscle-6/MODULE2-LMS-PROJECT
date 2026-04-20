package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.dto.CommentRequestDTO; // 1. 새로 만든 DTO 임포트
import com.wanted.ailienlmsprogram.community.dto.CommentResponseDTO;
import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.repository.CommentRepository;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
import com.wanted.ailienlmsprogram.global.filtering.BadWordCheck;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    // 댓글 쓰기 (DTO를 사용하도록 수정)
    @Transactional
    @BadWordCheck // ★ AOP가 DTO 내부의 content 필드를 감시합니다.
    public void saveComment(Long postId, CommentRequestDTO request, Member member) { // 2. String 대신 DTO로 변경
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        if (post.getPostTitle().startsWith("[공지]")) {
            throw new IllegalStateException("공지사항에는 댓글을 달 수 없습니다.");
        }

        CommunityComment comment = CommunityComment.create(content, post, member);
        commentRepository.save(comment);
    }

    // 댓글 목록 조회 (기존 유지)
    public List<CommentResponseDTO> findComments(Long postId) {
        return commentRepository.findByPostPostIdAndIsDeletedFalse(postId).stream()
                .map(comment -> CommentResponseDTO.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .memberName(comment.getMember().getName())
                        .memberId(comment.getMember().getMemberId())
                        .build())
                .collect(Collectors.toList());
    }

    // 댓글 삭제 (기존 유지)
    @Transactional
    public void deleteComment(Long commentId, Member loginMember) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getMember().getMemberId().equals(loginMember.getMemberId())) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        comment.delete();
    }
}