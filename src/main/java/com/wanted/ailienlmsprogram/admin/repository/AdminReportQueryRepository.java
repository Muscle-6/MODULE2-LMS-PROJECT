package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.AdminReportListResponse;
import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.entity.Report;
import com.wanted.ailienlmsprogram.community.entity.TargetType;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*관리자 신고 목록 화면에 필요한 조회 전용 리포지토리
*
* - 신고 엔티티를 기준으로 신고자, 게시글, 댓글, 대륙 정보를 수집한 뒤
* - 관리자 화면에 바로 출력할 수 있는 DTO로 가공한다.*/
@Repository
public class AdminReportQueryRepository {

    @PersistenceContext
    private EntityManager em;

    // 현재 신고 목록을 조회한다.
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
                .filter(id -> id != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Long> postIds = reports.stream()
                .filter(report -> report.getTargetType() == TargetType.CONTINENT_POST)
                .map(Report::getTargetNo)
                .filter(id -> id != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<Long> commentIds = reports.stream()
                .filter(report -> report.getTargetType() == TargetType.CONTINENT_COMMENT)
                .map(Report::getTargetNo)
                .filter(id -> id != null)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<Long, Member> reporterMap = findMembers(reporterIds);
        Map<Long, CommunityPost> postMap = findPosts(postIds);
        Map<Long, CommunityComment> commentMap = findComments(commentIds);
        Map<Long, Continent> continentMap = findContinents(postMap, commentMap);

        return reports.stream()
                .map(report -> toResponse(report, reporterMap, postMap, commentMap, continentMap))
                .toList();
    }

    //단일 신고 엔티티를 관리자 화면을 DTO로 변환한다.
    private AdminReportListResponse toResponse(
            Report report,
            Map<Long, Member> reporterMap,
            Map<Long, CommunityPost> postMap,
            Map<Long, CommunityComment> commentMap,
            Map<Long, Continent> continentMap
    ) {
        Member reporter = reporterMap.get(report.getReporterNo());
        TargetInfo targetInfo = resolveTargetInfo(report, postMap, commentMap, continentMap);

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
                targetInfo.continentName,
                targetInfo.targetTitle,
                targetInfo.targetPreview,
                targetInfo.targetDeleted
        );
    }

    // 신고 대상 타입에 따라 대상 정보 해석 로직을 분기한다.
    private TargetInfo resolveTargetInfo(
            Report report,
            Map<Long, CommunityPost> postMap,
            Map<Long, CommunityComment> commentMap,
            Map<Long, Continent> continentMap
    ) {
        if (report.getTargetType() == TargetType.CONTINENT_POST) {
            return buildPostTargetInfo(postMap.get(report.getTargetNo()), continentMap);
        }

        if (report.getTargetType() == TargetType.CONTINENT_COMMENT) {
            return buildCommentTargetInfo(commentMap.get(report.getTargetNo()), continentMap);
        }

        return buildQnaPlaceholderInfo();
    }

    //게시글 신고 대상 정보를 생성한다.
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

    // 댓글 신고 대상 정보를 생성한다.
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
                parentPost != null ? parentPost.getPostTitle() : "원문 게시글 없음",
                abbreviate(comment.getContent(), 90),
                comment.isDeleted()
        );
    }

    //Q&A 신고용 임시 대상 정보를 생성한다.
    private TargetInfo buildQnaPlaceholderInfo() {
        return new TargetInfo(
                null,
                null,
                null,
                null,
                "Q&A 신고",
                "현재 관리 페이지는 community 신고(post/comment) 기준으로 우선 연결했습니다.",
                false
        );
    }

    // 신고자 회원 정보를 ID 집합으로 일괄 조회한다.
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

    // 신고 대상 게시글 정보를 ID 집합으로 일괄 조회한다.
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

    // 신고 대상 댓글 정보를 ID 집합으로 일괄 조회한다.
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

    // 게시글/댓글로부터 연관 대륙 ID를 수집하여 대륙 정보를 일괄 조회한다.
    private Map<Long, Continent> findContinents(
            Map<Long, CommunityPost> postMap,
            Map<Long, CommunityComment> commentMap
    ) {
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

    // 긴 본문을 목록 화면에 맞게 잘라내어 미리보기 문자열로 변환한다.
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

    /*신고 대상의 화면 표시용 벙보를 임시로 묶는 내부 클래스.
    *
    * - 대상 회원, 대륙, 제목, 미리보기, 삭제 여부를 함께 들고 다니며
    * - 최종 AdminReportListResponse 조립에 사용된다.*/
    private static class TargetInfo {
        private final Long targetMemberId;
        private final String targetMemberLoginId;
        private final String targetMemberName;
        private final String continentName;
        private final String targetTitle;
        private final String targetPreview;
        private final boolean targetDeleted;

        private TargetInfo(
                Long targetMemberId,
                String targetMemberLoginId,
                String targetMemberName,
                String continentName,
                String targetTitle,
                String targetPreview,
                boolean targetDeleted
        ) {
            this.targetMemberId = targetMemberId;
            this.targetMemberLoginId = targetMemberLoginId;
            this.targetMemberName = targetMemberName;
            this.continentName = continentName;
            this.targetTitle = targetTitle;
            this.targetPreview = targetPreview;
            this.targetDeleted = targetDeleted;
        }

        // 대상이 이미 삭제되었거나 조회되지 않을 때 사용할 기본 정보를 생성한다.
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