package com.wanted.ailienlmsprogram.lectureprogress.service;

import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import com.wanted.ailienlmsprogram.enrollment.repository.EnrollmentRepository;
import com.wanted.ailienlmsprogram.lecture.repository.LectureRepository;
import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import com.wanted.ailienlmsprogram.lectureprogress.repository.LectureProgressRepository;
import com.wanted.ailienlmsprogram.lectureprogress.entity.LectureProgress;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LectureProgressService {

    private final LectureProgressRepository lectureProgressRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final EnrollmentRepository enrollmentRepository;

    // 수강 완료 처리
    @Transactional
    public void completeLecture(Long lectureId, Long memberId) {

        // 1. 강의 조회
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));

        // 2. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다."));

        // 3. 이미 완료된 강의면 중복 처리 방지
        Optional<LectureProgress> existing = lectureProgressRepository
                .findByMember_MemberIdAndLecture_LectureId(memberId, lectureId);

        if (existing.isPresent() && existing.get().getProgressIsCompleted()) {
            return;
        }

        // 4. LectureProgress 저장
        LectureProgress progress = LectureProgress.create(member, lecture);
        lectureProgressRepository.save(progress);

        // 5. 진척률 계산
        Long courseId = lecture.getCourse().getCourseId();

        int totalLectures = lectureRepository.countByCourse_CourseId(courseId);
        int completedLectures = lectureProgressRepository
                .countByMember_MemberIdAndLecture_Course_CourseIdAndProgressIsCompletedTrue(memberId, courseId);

        BigDecimal progressRate = BigDecimal.valueOf(completedLectures)
                .divide(BigDecimal.valueOf(totalLectures), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        // 6. Enrollment 진척률 업데이트
        Enrollment enrollment = enrollmentRepository
                .findByMemberMemberIdAndCourseCourseId(memberId, courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "수강 정보가 없습니다."));

        enrollment.updateProgressRate(progressRate);

        // 7. 진척률 100% 이면 등급 업그레이드
        if (progressRate.compareTo(BigDecimal.valueOf(100)) == 0) {
            member.upgradeRank();
        }
    }

    // 수강 완료 여부 확인
    public boolean isCompleted(Long lectureId, Long memberId) {
        return lectureProgressRepository
                .findByMember_MemberIdAndLecture_LectureId(memberId, lectureId)
                .map(LectureProgress::getProgressIsCompleted)
                .orElse(false);
    }

    // courseId 조회 (컨트롤러에서 redirect용으로 사용)
    public Long findCourseId(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 강의입니다."));
        return lecture.getCourse().getCourseId();
    }
}
