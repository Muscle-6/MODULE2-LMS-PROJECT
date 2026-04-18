package com.wanted.ailienlmsprogram.coursenotice.dao;

import com.wanted.ailienlmsprogram.coursenotice.entity.CourseNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseNoticeRepository extends JpaRepository<CourseNotice, Long> {

    // 특정 강좌의 공지사항 전체 조회 (최신순)
    List<CourseNotice> findByCourse_CourseIdOrderByCreatedAtDesc(Long courseId);
}
