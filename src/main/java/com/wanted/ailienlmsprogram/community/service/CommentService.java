package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.dto.CommentResponse;
import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.repository.CommentRepository;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
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

    // 댓글 쓰기 (공지글 체크)
    @Transactional
    public void saveComment(Long postId, String content, Member member) {
        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));


        if (post.getPostTitle().startsWith("[공지]")) {
            throw new IllegalStateException("공지사항에는 댓글을 달 수 없습니다.");
        }

        CommunityComment comment = CommunityComment.builder()
                .content(content)
                .post(post)
                .member(member)
                .isDeleted(false)
                .build();
        commentRepository.save(comment);
    }

    // 댓글 목록 조회
    public List<CommentResponse> findComments(Long postId) {

        return commentRepository.findByPostPostIdAndIsDeletedFalse(postId).stream()
                .map(comment -> CommentResponse.builder()
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .memberName(comment.getMember().getName()) // memberName -> name 으로 수정
                        .memberId(comment.getMember().getMemberId()) // memberId 유지
                        .build())
                .collect(Collectors.toList());
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Member loginMember) {
        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 본인 확인
        if (!comment.getMember().getMemberId().equals(loginMember.getMemberId())) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        comment.delete();
    }
}