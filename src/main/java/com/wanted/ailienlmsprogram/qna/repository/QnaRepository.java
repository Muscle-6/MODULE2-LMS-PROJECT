package com.wanted.ailienlmsprogram.qna.repository;

import com.wanted.ailienlmsprogram.qna.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna, Integer> {
    List<Qna> findAllByCourse_CourseId(Long courseId);
}
