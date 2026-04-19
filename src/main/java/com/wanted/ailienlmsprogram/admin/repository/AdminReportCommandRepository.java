package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.entity.Report;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AdminReportCommandRepository {

    @PersistenceContext
    private EntityManager em;

    public void deleteReportedTarget(Long reportId) {
        Report report = em.find(Report.class, reportId);

        if (report == null) {
            throw new IllegalArgumentException("존재하지 않는 신고입니다.");
        }

        switch (report.getTargetType()) {
            case CONTINENT_POST -> deletePost(report.getTargetNo());
            case CONTINENT_COMMENT -> deleteComment(report.getTargetNo());
            case QNA -> deleteQna(report.getTargetNo());
        }
    }

    private void deletePost(Long postId) {
        CommunityPost post = em.find(CommunityPost.class, postId);

        if (post == null) {
            throw new IllegalArgumentException("삭제할 게시글이 존재하지 않습니다.");
        }

        if (!post.isPostIsDeleted()) {
            post.setPostIsDeleted(true);
        }
    }

    private void deleteComment(Long commentId) {
        CommunityComment comment = em.find(CommunityComment.class, commentId);

        if (comment == null) {
            throw new IllegalArgumentException("삭제할 댓글이 존재하지 않습니다.");
        }

        if (!comment.isDeleted()) {
            comment.delete();
        }
    }

    private void deleteQna(Long qnaId) {
        Qna qna = em.find(Qna.class, qnaId);

        if (qna == null) {
            throw new IllegalArgumentException("삭제할 Q&A가 존재하지 않습니다.");
        }

        if (!qna.isQnaIsDeleted()) {
            qna.delete();
        }
    }
}