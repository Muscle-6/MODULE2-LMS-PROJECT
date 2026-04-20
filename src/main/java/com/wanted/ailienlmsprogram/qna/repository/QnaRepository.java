package com.wanted.ailienlmsprogram.qna.repository;

import com.wanted.ailienlmsprogram.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    //List<Qna> findAllByCourse_CourseIdAndParentIsNullAndQnaIsDeletedFalse(Long courseId);

    @Query("select q, " +
            "(select count(r) > 0 from Qna r where r.parent = q and r.qnaIsDeleted = false) " +
            "from Qna q " +
            "join fetch q.author " + // 작성자 한 방에 가져오기 (Fetch Join)
            "where q.course.courseId = :courseId " +
            "and q.parent is null " +
            "and q.qnaIsDeleted = false")
    List<Object[]> findAllByCourseIdWithAuthorAndReplyStatus(@Param("courseId") Long courseId);

    List<Qna> findAllByParent_QnaIdAndQnaIsDeletedFalse(Long qnaId);

    @Query("SELECT COUNT(q) > 0 FROM Qna q WHERE q.parent.qnaId = :qnaId AND q.qnaIsDeleted = false")
    boolean existsReplyByParentId(@Param("qnaId") Long qnaId);
}
