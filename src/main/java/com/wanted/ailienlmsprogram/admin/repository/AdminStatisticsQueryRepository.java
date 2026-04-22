package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.*;
import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.community.entity.Report;
import com.wanted.ailienlmsprogram.community.entity.TargetType;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import com.wanted.ailienlmsprogram.qna.entity.Qna;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.*;

/*실제 통계 쿼리 를 책임진다.*/
@Repository
public class AdminStatisticsQueryRepository {

    @PersistenceContext
    private EntityManager em;

    /*통계 화면의 대륙 선택 박스에 쓸 옵션 목록을 조회한다.*/
    public List<AdminContinentOptionResponse> findContinentOptions() {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentOptionResponse(
                    c.continentId,
                    c.continentName
                )
                from Continent c
                order by c.continentName asc
                """, AdminContinentOptionResponse.class)
                .getResultList();
    }

    /*대륙 랭킹을 metric 기준에 따라 분기 조회한다.
    *
    * course_count -> 공개 강좌 수
    * student_count -> 활성 수강생 수
    * post_count -> 삭제되지 않은 게시글 수
    * report_count -> 해당 대륙 관련 신고를 남긴 신고자 수(중복 제거)*/
    public List<AdminContinentRankingResponse> findContinentRankings(AdminContinentStatisticMetric metric) {
        if (metric == null) {
            metric = AdminContinentStatisticMetric.COURSE_COUNT;
        }

        return switch (metric) {
            case COURSE_COUNT -> findContinentCourseCountRankings();
            case STUDENT_COUNT -> findContinentStudentCountRankings();
            case POST_COUNT -> findContinentPostCountRankings();
            case REPORTER_COUNT -> findContinentReporterCountRankings();
        };
    }

    /*선택된 대륙 안에서 강좌 랭킹을 metric 기준에 따라 분기 조회한다.
    *
    * Student_count -> 강좌별 수강생 수
    * QNA_count -> 강좌별 Q&A 수*/
    public List<AdminCourseRankingResponse> findCourseRankings(Long continentId, AdminCourseStatisticMetric metric) {
        if (continentId == null) {
            return Collections.emptyList();
        }

        if (metric == null) {
            metric = AdminCourseStatisticMetric.STUDENT_COUNT;
        }

        return switch (metric) {
            case STUDENT_COUNT -> findCourseStudentCountRankings(continentId);
            case QNA_COUNT -> findCourseQnaCountRankings(continentId);
        };
    }

    /*공개 상태 강좌 수 기준으로 대륙 랭킹을 조회한다.*/
    private List<AdminContinentRankingResponse> findContinentCourseCountRankings() {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentRankingResponse(
                    ct.continentId,
                    ct.continentName,
                    count(distinct c.courseId)
                )
                from Continent ct
                left join Course c
                    on c.continent = ct
                   and c.courseStatus = :publishedStatus
                group by ct.continentId, ct.continentName
                order by count(distinct c.courseId) desc, ct.continentName asc
                """, AdminContinentRankingResponse.class)
                .setParameter("publishedStatus", CourseStatus.PUBLISHED)
                .getResultList();
    }

    /*활성 수강생 수 기준으로 대륙 랭킹을 조회한다.*/
    private List<AdminContinentRankingResponse> findContinentStudentCountRankings() {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentRankingResponse(
                    ct.continentId,
                    ct.continentName,
                    count(distinct e.member.memberId)
                )
                from Continent ct
                left join Course c
                    on c.continent = ct
                left join Enrollment e
                    on e.course = c
                   and e.status = :activeStatus
                group by ct.continentId, ct.continentName
                order by count(distinct e.member.memberId) desc, ct.continentName asc
                """, AdminContinentRankingResponse.class)
                .setParameter("activeStatus", Enrollment.EnrollmentStatus.ACTIVE)
                .getResultList();
    }

    /*삭제되지 않은 게시글 수 기준으로 대륙 랭킹을 조회한다.*/
    private List<AdminContinentRankingResponse> findContinentPostCountRankings() {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentRankingResponse(
                    ct.continentId,
                    ct.continentName,
                    count(distinct p.postId)
                )
                from Continent ct
                left join CommunityPost p
                    on p.continentId = ct.continentId
                   and p.postIsDeleted = false
                group by ct.continentId, ct.continentName
                order by count(distinct p.postId) desc, ct.continentName asc
                """, AdminContinentRankingResponse.class)
                .getResultList();
    }

    /*신고자 수 기준 대륙 랭킹을 만든다.
    *
    * report 테이블에는 continentId가 직접 없으므로
    * 게시글/댓글/Q&A 대상에서 대륙 ID를 역추적한 뒤
    * 신고자 ID를 Set으로 모아 중복 제거해야한다.*/
    private List<AdminContinentRankingResponse> findContinentReporterCountRankings() {
        List<AdminContinentOptionResponse> continents = findContinentOptions();
        Map<Long, Set<Long>> reporterSetByContinent = buildReporterSetByContinent();

        List<AdminContinentRankingResponse> result = new ArrayList<>();

        for (AdminContinentOptionResponse continent : continents) {
            long reporterCount = reporterSetByContinent
                    .getOrDefault(continent.getContinentId(), Collections.emptySet())
                    .size();

            result.add(new AdminContinentRankingResponse(
                    continent.getContinentId(),
                    continent.getContinentName(),
                    reporterCount
            ));
        }

        result.sort(Comparator
                .comparing(AdminContinentRankingResponse::getMetricValueOrZero, Comparator.reverseOrder())
                .thenComparing(AdminContinentRankingResponse::getContinentName));

        return result;
    }

    /*대륙별 신고자 집합을 만든다. 한 신고자가 여러 번 신고해도 1명으로 센다.*/
    private Map<Long, Set<Long>> buildReporterSetByContinent() {
        List<Report> reports = em.createQuery("""
                select r
                from Report r
                """, Report.class).getResultList();

        if (reports.isEmpty()) {
            return Collections.emptyMap();
        }

        Set<Long> postIds = new LinkedHashSet<>();
        Set<Long> commentIds = new LinkedHashSet<>();
        Set<Long> qnaIds = new LinkedHashSet<>();

        for (Report report : reports) {
            if (report.getTargetNo() == null) {
                continue;
            }

            if (report.getTargetType() == TargetType.CONTINENT_POST) {
                postIds.add(report.getTargetNo());
            } else if (report.getTargetType() == TargetType.CONTINENT_COMMENT) {
                commentIds.add(report.getTargetNo());
            } else if (report.getTargetType() == TargetType.QNA) {
                qnaIds.add(report.getTargetNo());
            }
        }

        Map<Long, Long> postContinentMap = findPostContinentMap(postIds);
        Map<Long, Long> commentContinentMap = findCommentContinentMap(commentIds);
        Map<Long, Long> qnaContinentMap = findQnaContinentMap(qnaIds);

        Map<Long, Set<Long>> result = new HashMap<>();

        for (Report report : reports) {
            if (report.getReporterNo() == null) {
                continue;
            }

            Long continentId = resolveContinentId(report, postContinentMap, commentContinentMap, qnaContinentMap);
            if (continentId == null) {
                continue;
            }

            result.computeIfAbsent(continentId, key -> new LinkedHashSet<>())
                    .add(report.getReporterNo());
        }

        return result;
    }

    /*게시글 ID -> 대륙 ID 맵을 만든다.*/
    private Map<Long, Long> findPostContinentMap(Set<Long> postIds) {
        if (postIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<CommunityPost> posts = em.createQuery("""
                select p
                from CommunityPost p
                where p.postId in :postIds
                """, CommunityPost.class)
                .setParameter("postIds", postIds)
                .getResultList();

        Map<Long, Long> result = new HashMap<>();
        for (CommunityPost post : posts) {
            result.put(post.getPostId(), post.getContinentId());
        }
        return result;
    }

    /*댓글 ID -> 대륙 ID 맵을 만든다.*/
    private Map<Long, Long> findCommentContinentMap(Set<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<CommunityComment> comments = em.createQuery("""
                select distinct c
                from CommunityComment c
                left join fetch c.post
                where c.commentId in :commentIds
                """, CommunityComment.class)
                .setParameter("commentIds", commentIds)
                .getResultList();

        Map<Long, Long> result = new HashMap<>();
        for (CommunityComment comment : comments) {
            Long continentId = null;
            if (comment.getPost() != null) {
                continentId = comment.getPost().getContinentId();
            }
            result.put(comment.getCommentId(), continentId);
        }
        return result;
    }

    /*Q&A ID -> 대륙 ID 맵을 만든다.*/
    private Map<Long, Long> findQnaContinentMap(Set<Long> qnaIds) {
        if (qnaIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Qna> qnas = em.createQuery("""
                select distinct q
                from Qna q
                join fetch q.course c
                join fetch c.continent ct
                where q.qnaId in :qnaIds
                """, Qna.class)
                .setParameter("qnaIds", qnaIds)
                .getResultList();

        Map<Long, Long> result = new HashMap<>();
        for (Qna qna : qnas) {
            Long continentId = qna.getCourse().getContinent().getContinentId();
            result.put(qna.getQnaId(), continentId);
        }
        return result;
    }

    /*Report 1건이 어느 대륙에 속하는지 targetType에 따라 판별한다.*/
    private Long resolveContinentId(
            Report report,
            Map<Long, Long> postContinentMap,
            Map<Long, Long> commentContinentMap,
            Map<Long, Long> qnaContinentMap
    ) {
        if (report.getTargetType() == TargetType.CONTINENT_POST) {
            return postContinentMap.get(report.getTargetNo());
        }

        if (report.getTargetType() == TargetType.CONTINENT_COMMENT) {
            return commentContinentMap.get(report.getTargetNo());
        }

        if (report.getTargetType() == TargetType.QNA) {
            return qnaContinentMap.get(report.getTargetNo());
        }

        return null;
    }

    /*선택 대륙 안의 강좌를 수강생 수 기준으로 정렬해 조회한다.*/
    private List<AdminCourseRankingResponse> findCourseStudentCountRankings(Long continentId) {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminCourseRankingResponse(
                    c.courseTitle,
                    c.instructor.name,
                    count(distinct e.member.memberId)
                )
                from Course c
                left join Enrollment e
                    on e.course = c
                   and e.status = :activeStatus
                where c.continent.continentId = :continentId
                group by c.courseId, c.courseTitle, c.instructor.name
                order by count(distinct e.member.memberId) desc, c.courseTitle asc
                """, AdminCourseRankingResponse.class)
                .setParameter("continentId", continentId)
                .setParameter("activeStatus", Enrollment.EnrollmentStatus.ACTIVE)
                .getResultList();
    }

    /*선택 대륙 안의 강좌를 Q&A 수 기준으로 정렬해 조회한다.*/
    private List<AdminCourseRankingResponse> findCourseQnaCountRankings(Long continentId) {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminCourseRankingResponse(
                    c.courseTitle,
                    c.instructor.name,
                    count(q.qnaId)
                )
                from Course c
                left join Qna q
                    on q.course = c
                   and q.qnaIsDeleted = false
                where c.continent.continentId = :continentId
                group by c.courseId, c.courseTitle, c.instructor.name
                order by count(q.qnaId) desc, c.courseTitle asc
                """, AdminCourseRankingResponse.class)
                .setParameter("continentId", continentId)
                .getResultList();
    }
}