package com.wanted.ailienlmsprogram.qna.repository;

import com.wanted.ailienlmsprogram.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Long> {

    List<Qna> findAllByCourse_CourseIdAndParentIsNullAndQnaIsDeletedFalse(Long courseId);

    List<Qna> findAllByParent_QnaIdAndQnaIsDeletedFalse(Long qnaId);

    @Query("SELECT COUNT(q) > 0 FROM Qna q WHERE q.parent.qnaId = :qnaId AND q.qnaIsDeleted = false")
    boolean existsReplyByParentId(@Param("qnaId") Long qnaId);

}
