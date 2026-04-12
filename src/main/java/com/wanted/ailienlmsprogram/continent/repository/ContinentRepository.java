package com.wanted.ailienlmsprogram.continent.repository;

import com.wanted.ailienlmsprogram.continent.dto.ContinentCourseSummary;
import com.wanted.ailienlmsprogram.continent.dto.ContinentPostSummary;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContinentRepository extends JpaRepository<Continent, Long> {

    List<Continent> findByContinentNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrderByContinentNameAsc(
            String continentNameKeyword,
            String descriptionKeyword
    );

    @Query(value = """
        SELECT
            c.강좌번호 AS courseId,
            c.강좌제목 AS courseTitle,
            c.가격 AS price,
            m.이름 AS instructorName
        FROM COURSE c
        JOIN MEMBER m
          ON c.강사번호 = m.회원번호
        WHERE c.대륙번호 = :continentId
          AND c.공개상태 = 'PUBLISHED'
        ORDER BY c.등록일시 DESC, c.강좌번호 DESC
        """, nativeQuery = true)
    List<ContinentCourseSummary> findPublishedCoursesByContinentId(@Param("continentId") Long continentId);

    @Query(value = """
        SELECT
            p.게시글번호 AS postId,
            p.제목 AS title,
            m.이름 AS writerName,
            p.공지여부 AS notice,
            p.작성일시 AS createdAt
        FROM CONTINENT_POST p
        JOIN MEMBER m
          ON p.작성자번호 = m.회원번호
        WHERE p.대륙번호 = :continentId
          AND p.삭제여부 = false
        ORDER BY p.공지여부 DESC, p.작성일시 DESC, p.게시글번호 DESC
        LIMIT 10
        """, nativeQuery = true)
    List<ContinentPostSummary> findTop10PostsByContinentId(@Param("continentId") Long continentId);
}