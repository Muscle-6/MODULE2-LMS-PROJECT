package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.dto.CommentRequestDTO; // 1. 새로 만든 DTO 임포트
import com.wanted.ailienlmsprogram.community.dto.CommentResponseDTO;
import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.repository.CommentRepository;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
import com.wanted.ailienlmsprogram.global.filtering.BadWordCheck;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
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


    @Transactional
    @BadWordCheck
    public void saveComment(Long postId, CommentRequestDTO request, Member member) { // 2. String 대신 DTO로 변경
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "게시글이 없습니다."));

        if (post.getPostTitle().startsWith("[공지]")) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "공지사항에는 댓글을 달 수 없습니다.");
        }

        CommunityComment comment = CommunityComment.create(request.getContent(), post, member);
        commentRepository.save(comment);
    }

    // 댓글 목록 조회
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

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Member loginMember) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (!comment.getMember().getMemberId().equals(loginMember.getMemberId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        comment.delete();
    }
}