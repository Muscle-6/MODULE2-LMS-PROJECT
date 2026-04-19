package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.AdminReportDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminReportListResponse;
import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.entity.Report;
import com.wanted.ailienlmsprogram.community.entity.TargetType;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * 관리자 신고 목록/상세 화면에 필요한 조회 전용 리포지토리
 */
@Repository
public class AdminReportQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<AdminReportListResponse> findReports() {
        List<Report> reports = em.createQuery("""
                select r
                from Report r
                order by r.reportDate desc, r.reportNo desc
                """, Report.class)
                .getResultList();

        if (reports.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> reporterIds = reports.stream()
                .map(Report::getReporterNo)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Long> postIds = reports.stream()
                .filter(report -> report.getTargetType() == TargetType.CONTINENT_POST)
                .map(Report::getTargetNo)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Long> commentIds = reports.stream()
                .filter(report -> report.getTargetType() == TargetType.CONTINENT_COMMENT)
                .map(Report::getTargetNo)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Long> qnaIds = reports.stream()
                .filter(report -> report.getTargetType() == TargetType.QNA)
                .map(Report::getTargetNo)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<Long, Member> reporterMap = findMembers(reporterIds);
        Map<Long, CommunityPost> postMap = findPosts(postIds);
        Map<Long, CommunityComment> commentMap = findComments(commentIds);
        Map<Long, Qna> qnaMap = findQnas(qnaIds);
        Map<Long, Continent> continentMap = findContinents(postMap, commentMap);

        return reports.stream()
                .map(report -> toResponse(report, reporterMap, postMap, commentMap, qnaMap, continentMap))
                .toList();
    }

    public AdminReportDetailResponse findReportDetail(Long reportId) {
        Report report = em.find(Report.class, reportId);

        if (report == null) {
            throw new IllegalArgumentException("존재하지 않는 신고입니다.");
        }

        Member reporter = report.getReporterNo() != null ? em.find(Member.class, report.getReporterNo()) : null;

        return switch (report.getTargetType()) {
            case CONTINENT_POST -> buildPostDetail(report, reporter);
            case CONTINENT_COMMENT -> buildCommentDetail(report, reporter);
            case QNA -> buildQnaDetail(report, reporter);
        };
    }

    private AdminReportListResponse toResponse(Report report,
                                               Map<Long, Member> reporterMap,
                                               Map<Long, CommunityPost> postMap,
                                               Map<Long, CommunityComment> commentMap,
                                               Map<Long, Qna> qnaMap,
                                               Map<Long, Continent> continentMap) {
        Member reporter = reporterMap.get(report.getReporterNo());
        TargetInfo targetInfo = resolveTargetInfo(report, postMap, commentMap, qnaMap, continentMap);

        return new AdminReportListResponse(
                report.getReportNo(),
                report.getTargetType(),
                report.getReason(),
                report.getReportDate(),
                reporter != null ? reporter.getLoginId() : null,
                reporter != null ? reporter.getName() : null,
                targetInfo.targetMemberId,
                targetInfo.targetMemberLoginId,
                targetInfo.targetMemberName,
                targetInfo.locationName,
                targetInfo.targetTitle,
                targetInfo.targetPreview,
                targetInfo.targetDeleted
        );
    }

    private TargetInfo resolveTargetInfo(Report report,
                                         Map<Long, CommunityPost> postMap,
                                         Map<Long, CommunityComment> commentMap,
                                         Map<Long, Qna> qnaMap,
                                         Map<Long, Continent> continentMap) {
        if (report.getTargetType() == TargetType.CONTINENT_POST) {
            return buildPostTargetInfo(postMap.get(report.getTargetNo()), continentMap);
        }

        if (report.getTargetType() == TargetType.CONTINENT_COMMENT) {
            return buildCommentTargetInfo(commentMap.get(report.getTargetNo()), continentMap);
        }

        return buildQnaTargetInfo(qnaMap.get(report.getTargetNo()));
    }

    private TargetInfo buildPostTargetInfo(CommunityPost post, Map<Long, Continent> continentMap) {
        if (post == null) {
            return TargetInfo.missing();
        }

        Member targetMember = post.getMember();
        Continent continent = continentMap.get(post.getContinentId());

        return new TargetInfo(
                targetMember != null ? targetMember.getMemberId() : null,
                targetMember != null ? targetMember.getLoginId() : null,
                targetMember != null ? targetMember.getName() : null,
                continent != null ? continent.getContinentName() : null,
                post.getPostTitle(),
                abbreviate(post.getPostContent(), 90),
                post.isPostIsDeleted()
        );
    }

    private TargetInfo buildCommentTargetInfo(CommunityComment comment, Map<Long, Continent> continentMap) {
        if (comment == null) {
            return TargetInfo.missing();
        }

        Member targetMember = comment.getMember();
        CommunityPost parentPost = comment.getPost();
        Long continentId = parentPost != null ? parentPost.getContinentId() : null;
        Continent continent = continentId != null ? continentMap.get(continentId) : null;

        return new TargetInfo(
                targetMember != null ? targetMember.getMemberId() : null,
                targetMember != null ? targetMember.getLoginId() : null,
                targetMember != null ? targetMember.getName() : null,
                continent != null ? continent.getContinentName() : null,
                parentPost != null ? parentPost.getPostTitle() : "댓글",
                abbreviate(comment.getContent(), 90),
                comment.isDeleted()
        );
    }

    private TargetInfo buildQnaTargetInfo(Qna qna) {
        if (qna == null) {
            return TargetInfo.missing();
        }

        Member targetMember = qna.getAuthor();
        String courseTitle = qna.getCourse() != null ? qna.getCourse().getCourseTitle() : null;

        return new TargetInfo(
                targetMember != null ? targetMember.getMemberId() : null,
                targetMember != null ? targetMember.getLoginId() : null,
                targetMember != null ? targetMember.getName() : null,
                courseTitle,
                qna.getQnaTitle(),
                abbreviate(qna.getQnaContent(), 90),
                qna.isQnaIsDeleted()
        );
    }

    private AdminReportDetailResponse buildPostDetail(Report report, Member reporter) {
        CommunityPost post = findPost(report.getTargetNo());

        if (post == null) {
            return missingDetail(report, reporter, "게시글");
        }

        Member targetMember = post.getMember();
        Continent continent = post.getContinentId() != null ? em.find(Continent.class, post.getContinentId()) : null;

        return AdminReportDetailResponse.builder()
                .reportId(report.getReportNo())
                .targetType(report.getTargetType())
                .reportReason(report.getReason())
                .reportedAt(report.getReportDate())
                .reporterLoginId(reporter != null ? reporter.getLoginId() : null)
                .reporterName(reporter != null ? reporter.getName() : null)
                .targetMemberId(targetMember != null ? targetMember.getMemberId() : null)
                .targetMemberLoginId(targetMember != null ? targetMember.getLoginId() : null)
                .targetMemberName(targetMember != null ? targetMember.getName() : null)
                .categoryLabel("대륙")
                .categoryValue(continent != null ? continent.getContinentName() : null)
                .targetTitle(post.getPostTitle())
                .targetContent(post.getPostContent())
                .parentTitle(null)
                .targetDeleted(post.isPostIsDeleted())
                .build();
    }

    private AdminReportDetailResponse buildCommentDetail(Report report, Member reporter) {
        CommunityComment comment = findComment(report.getTargetNo());

        if (comment == null) {
            return missingDetail(report, reporter, "댓글");
        }

        Member targetMember = comment.getMember();
        CommunityPost parentPost = comment.getPost();
        Long continentId = parentPost != null ? parentPost.getContinentId() : null;
        Continent continent = continentId != null ? em.find(Continent.class, continentId) : null;

        return AdminReportDetailResponse.builder()
                .reportId(report.getReportNo())
                .targetType(report.getTargetType())
                .reportReason(report.getReason())
                .reportedAt(report.getReportDate())
                .reporterLoginId(reporter != null ? reporter.getLoginId() : null)
                .reporterName(reporter != null ? reporter.getName() : null)
                .targetMemberId(targetMember != null ? targetMember.getMemberId() : null)
                .targetMemberLoginId(targetMember != null ? targetMember.getLoginId() : null)
                .targetMemberName(targetMember != null ? targetMember.getName() : null)
                .categoryLabel("대륙")
                .categoryValue(continent != null ? continent.getContinentName() : null)
                .targetTitle("댓글")
                .targetContent(comment.getContent())
                .parentTitle(parentPost != null ? parentPost.getPostTitle() : null)
                .targetDeleted(comment.isDeleted())
                .build();
    }

    private AdminReportDetailResponse buildQnaDetail(Report report, Member reporter) {
        Qna qna = findQna(report.getTargetNo());

        if (qna == null) {
            return missingDetail(report, reporter, "Q&A");
        }

        Member targetMember = qna.getAuthor();
        String courseTitle = qna.getCourse() != null ? qna.getCourse().getCourseTitle() : null;
        String parentTitle = qna.getParent() != null ? qna.getParent().getQnaTitle() : null;

        return AdminReportDetailResponse.builder()
                .reportId(report.getReportNo())
                .targetType(report.getTargetType())
                .reportReason(report.getReason())
                .reportedAt(report.getReportDate())
                .reporterLoginId(reporter != null ? reporter.getLoginId() : null)
                .reporterName(reporter != null ? reporter.getName() : null)
                .targetMemberId(targetMember != null ? targetMember.getMemberId() : null)
                .targetMemberLoginId(targetMember != null ? targetMember.getLoginId() : null)
                .targetMemberName(targetMember != null ? targetMember.getName() : null)
                .categoryLabel("강좌")
                .categoryValue(courseTitle)
                .targetTitle(qna.getQnaTitle())
                .targetContent(qna.getQnaContent())
                .parentTitle(parentTitle)
                .targetDeleted(qna.isQnaIsDeleted())
                .build();
    }

    private AdminReportDetailResponse missingDetail(Report report, Member reporter, String targetLabel) {
        return AdminReportDetailResponse.builder()
                .reportId(report.getReportNo())
                .targetType(report.getTargetType())
                .reportReason(report.getReason())
                .reportedAt(report.getReportDate())
                .reporterLoginId(reporter != null ? reporter.getLoginId() : null)
                .reporterName(reporter != null ? reporter.getName() : null)
                .targetMemberId(null)
                .targetMemberLoginId(null)
                .targetMemberName(null)
                .categoryLabel("분류")
                .categoryValue(null)
                .targetTitle(targetLabel + "이(가) 이미 삭제되었거나 존재하지 않습니다.")
                .targetContent(null)
                .parentTitle(null)
                .targetDeleted(true)
                .build();
    }

    private Map<Long, Member> findMembers(Set<Long> memberIds) {
        if (memberIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return em.createQuery("""
                select m
                from Member m
                where m.memberId in :memberIds
                """, Member.class)
                .setParameter("memberIds", memberIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(Member::getMemberId, Function.identity()));
    }

    private Map<Long, CommunityPost> findPosts(Set<Long> postIds) {
        if (postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return em.createQuery("""
                select distinct p
                from CommunityPost p
                left join fetch p.member
                where p.postId in :postIds
                """, CommunityPost.class)
                .setParameter("postIds", postIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(CommunityPost::getPostId, Function.identity()));
    }

    private Map<Long, CommunityComment> findComments(Set<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return em.createQuery("""
                select distinct c
                from CommunityComment c
                left join fetch c.member
                left join fetch c.post
                where c.commentId in :commentIds
                """, CommunityComment.class)
                .setParameter("commentIds", commentIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(CommunityComment::getCommentId, Function.identity()));
    }

    private Map<Long, Qna> findQnas(Set<Long> qnaIds) {
        if (qnaIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return em.createQuery("""
                select distinct q
                from Qna q
                left join fetch q.author
                left join fetch q.course
                left join fetch q.parent
                where q.qnaId in :qnaIds
                """, Qna.class)
                .setParameter("qnaIds", qnaIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(Qna::getQnaId, Function.identity()));
    }

    private Map<Long, Continent> findContinents(Map<Long, CommunityPost> postMap,
                                                Map<Long, CommunityComment> commentMap) {
        Set<Long> continentIds = new LinkedHashSet<>();

        for (CommunityPost post : postMap.values()) {
            if (post.getContinentId() != null) {
                continentIds.add(post.getContinentId());
            }
        }

        for (CommunityComment comment : commentMap.values()) {
            if (comment.getPost() != null && comment.getPost().getContinentId() != null) {
                continentIds.add(comment.getPost().getContinentId());
            }
        }

        if (continentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return em.createQuery("""
                select c
                from Continent c
                where c.continentId in :continentIds
                """, Continent.class)
                .setParameter("continentIds", continentIds)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(Continent::getContinentId, Function.identity()));
    }

    private CommunityPost findPost(Long postId) {
        List<CommunityPost> result = em.createQuery("""
                select distinct p
                from CommunityPost p
                left join fetch p.member
                where p.postId = :postId
                """, CommunityPost.class)
                .setParameter("postId", postId)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    private CommunityComment findComment(Long commentId) {
        List<CommunityComment> result = em.createQuery("""
                select distinct c
                from CommunityComment c
                left join fetch c.member
                left join fetch c.post
                where c.commentId = :commentId
                """, CommunityComment.class)
                .setParameter("commentId", commentId)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    private Qna findQna(Long qnaId) {
        List<Qna> result = em.createQuery("""
                select distinct q
                from Qna q
                left join fetch q.author
                left join fetch q.course
                left join fetch q.parent
                where q.qnaId = :qnaId
                """, Qna.class)
                .setParameter("qnaId", qnaId)
                .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    private String abbreviate(String value, int maxLength) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = value.replace("\r", " ")
                .replace("\n", " ")
                .trim();

        if (normalized.length() <= maxLength) {
            return normalized;
        }

        return normalized.substring(0, maxLength) + "...";
    }

    private static class TargetInfo {
        private final Long targetMemberId;
        private final String targetMemberLoginId;
        private final String targetMemberName;
        private final String locationName;
        private final String targetTitle;
        private final String targetPreview;
        private final boolean targetDeleted;

        private TargetInfo(Long targetMemberId,
                           String targetMemberLoginId,
                           String targetMemberName,
                           String locationName,
                           String targetTitle,
                           String targetPreview,
                           boolean targetDeleted) {
            this.targetMemberId = targetMemberId;
            this.targetMemberLoginId = targetMemberLoginId;
            this.targetMemberName = targetMemberName;
            this.locationName = locationName;
            this.targetTitle = targetTitle;
            this.targetPreview = targetPreview;
            this.targetDeleted = targetDeleted;
        }

        private static TargetInfo missing() {
            return new TargetInfo(
                    null,
                    null,
                    null,
                    null,
                    "삭제되었거나 존재하지 않는 대상",
                    null,
                    true
            );
        }
    }
}