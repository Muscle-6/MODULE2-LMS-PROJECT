package com.wanted.ailienlmsprogram.lectureprogress.repository;

import com.wanted.ailienlmsprogram.lectureprogress.entity.LectureProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureProgressRepository extends JpaRepository<LectureProgress, Long> {

    // 해당 강의를 이미 완료했는지 확인
    Optional<LectureProgress> findByMember_MemberIdAndLecture_LectureId(Long memberId, Long lectureId);

    // 해당 강좌에서 완료된 강의 수 조회 (진척률 계산용)
    int countByMember_MemberIdAndLecture_Course_CourseIdAndProgressIsCompletedTrue(Long memberId, Long courseId);
}
