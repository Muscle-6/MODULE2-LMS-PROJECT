package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.AdminCourseListResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminCourseSearchCondition;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class AdminCourseQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<AdminCourseListResponse> findCourses(AdminCourseSearchCondition condition) {

        StringBuilder jpql = new StringBuilder("""
            select new com.wanted.ailienlmsprogram.admin.dto.AdminCourseListResponse(
                c.courseId,
                c.courseTitle,
                c.continent.continentName,
                c.instructor.name,
                c.coursePrice,
                c.courseStatus,
                c.createdAt
            )
            from Course c
            where 1=1
            """);

        if (condition.getStatus() != null) {
            jpql.append(" and c.courseStatus = :status ");
        }

        if (StringUtils.hasText(condition.getQuery())) {
            jpql.append("""
                and (
                    lower(c.courseTitle) like lower(concat('%', :query, '%'))
                    or lower(c.continent.continentName) like lower(concat('%', :query, '%'))
                    or lower(c.instructor.name) like lower(concat('%', :query, '%'))
                )
                """);
        }

        jpql.append("""
            order by
                case when c.courseStatus = :draftStatus then 0 else 1 end,
                c.createdAt desc,
                c.courseId desc
            """);

        TypedQuery<AdminCourseListResponse> query =
                em.createQuery(jpql.toString(), AdminCourseListResponse.class);

        query.setParameter("draftStatus", CourseStatus.DRAFT);

        if (condition.getStatus() != null) {
            query.setParameter("status", condition.getStatus());
        }

        if (StringUtils.hasText(condition.getQuery())) {
            query.setParameter("query", condition.getQuery().trim());
        }

        return query.getResultList();
    }

    public Course findCourse(Long courseId) {
        return em.find(Course.class, courseId);
    }

    public int updateCourseStatus(Long courseId, CourseStatus targetStatus) {
        int updatedCount = em.createQuery("""
                update Course c
                   set c.courseStatus = :targetStatus
                 where c.courseId = :courseId
                """)
                .setParameter("targetStatus", targetStatus)
                .setParameter("courseId", courseId)
                .executeUpdate();

        em.clear();
        return updatedCount;
    }
}