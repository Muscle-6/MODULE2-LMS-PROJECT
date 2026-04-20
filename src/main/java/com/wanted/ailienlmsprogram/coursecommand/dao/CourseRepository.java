package com.wanted.ailienlmsprogram.coursecommand.dao;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    // 활성화 상태 쿼리문 추가
    @Query("SELECT c FROM Course c JOIN FETCH c.continent WHERE c.continent.continentId = :continentId AND c.courseStatus = 'PUBLISHED'")
    List<Course> findByContinent_ContinentId(@Param("continentId") Long continentId);

    // 상태별 조회 (공개/비공개 필터용)
    List<Course> findByInstructor_MemberIdAndCourseStatus(Long memberId, CourseStatus courseStatus);

    boolean existsByCourseIdAndInstructor_MemberId(Long courseId, Long instructorMemberId);

    @Query("select c from Course c " +
            "join fetch c.instructor " +
            "where c.instructor.memberId = :memberId " +
            "and c.courseStatus = :courseStatus")
    List<Course> findMyCoursesWithInstructor(@Param("memberId") Long memberId,
                                             @Param("courseStatus") CourseStatus courseStatus);
}
