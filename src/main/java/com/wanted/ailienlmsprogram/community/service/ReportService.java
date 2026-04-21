package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.entity.Report;
import com.wanted.ailienlmsprogram.community.entity.TargetType;
import com.wanted.ailienlmsprogram.community.repository.CommentRepository;
import com.wanted.ailienlmsprogram.community.repository.PostRepository;
import com.wanted.ailienlmsprogram.community.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;       // 게시글 조회를 위해 추가
    private final CommentRepository commentRepository; // 댓글 조회를 위해 추가

    @Transactional
    public void createReport(Long reporterNo, String targetType, Long targetNo, String reason) {
        String reportedTitle = null;
        String reportedContent = null;
        TargetType type = TargetType.valueOf(targetType);

        // 1. 신고 대상이 게시글(POST)일 경우
        if (type == TargetType.CONTINENT_POST) {
            CommunityPost post = postRepository.findById(targetNo)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. ID: " + targetNo));

            // 신고 즉시 제목과 내용을 변수에 담기 (박제 준비)
            reportedTitle = post.getPostTitle();
            reportedContent = post.getPostContent();
        }
        // 2. 신고 대상이 댓글(COMMENT)일 경우
        else if (type == TargetType.CONTINENT_COMMENT) {
            CommunityComment comment = commentRepository.findById(targetNo)
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. ID: " + targetNo));

            reportedTitle = "댓글 신고입니다."; // 댓글은 제목이 없으므로 고정 문구
            reportedContent = comment.getContent(); // CommunityComment의 필드명인 content 사용
        }

        // 3. 수정한 Report.create 메서드에 박제할 내용까지 담아서 생성
        Report report = Report.create(
                reporterNo,
                type,
                targetNo,
                reason,
                reportedTitle,
                reportedContent
        );

        // 4. DB에 저장 (이제 스냅샷이 함께 저장됩니다!)
        reportRepository.save(report);
    }
}