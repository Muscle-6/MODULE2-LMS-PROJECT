package com.wanted.ailienlmsprogram.coursenotice.service;

import com.wanted.ailienlmsprogram.coursecommand.repository.CourseRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursenotice.repository.CourseNoticeRepository;
import com.wanted.ailienlmsprogram.coursenotice.dto.CourseNoticeApplyDTO;
import com.wanted.ailienlmsprogram.coursenotice.dto.CourseNoticeDetailDTO;
import com.wanted.ailienlmsprogram.coursenotice.dto.CourseNoticeFindDTO;
import com.wanted.ailienlmsprogram.coursenotice.entity.CourseNotice;
import com.wanted.ailienlmsprogram.enrollment.entity.Enrollment;
import com.wanted.ailienlmsprogram.enrollment.repository.EnrollmentRepository;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseNoticeService {

    private final CourseNoticeRepository courseNoticeRepository;
    private final CourseRepository courseRepository;
    private final MemberRepository memberRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    // ===== 조회 =====

    // 특정 강좌의 공지사항 전체 조회
    public List<CourseNoticeFindDTO> findNotices(Long courseId, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 회원입니다."));

        boolean isEnrolled = enrollmentRepository.existsByMember_LoginIdAndCourse_CourseIdAndStatus(
                member.getLoginId(),
                courseId,
                Enrollment.EnrollmentStatus.ACTIVE
        );
        boolean isInstructor = courseRepository.existsByCourseIdAndInstructor_MemberId(courseId, memberId);

        if (!isEnrolled && !isInstructor) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "수강생만 접근 가능한 구역입니다.");
        }

        List<CourseNotice> notices = courseNoticeRepository
                .findByCourseIdWithFetchJoin(courseId);

        return notices.stream()
                .map(notice -> {
                    CourseNoticeFindDTO dto = modelMapper.map(notice, CourseNoticeFindDTO.class);
                    dto.setAuthorName(notice.getAuthor().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 공지사항 단건 조회 (수정 페이지용)
    public CourseNoticeFindDTO findNoticeById(Long noticeId) {

        CourseNotice notice = courseNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 공지사항입니다."));

        CourseNoticeFindDTO dto = modelMapper.map(notice, CourseNoticeFindDTO.class);
        dto.setAuthorName(notice.getAuthor().getName());
        return dto;
    }

    // ===== 등록 =====

    @Transactional
    public void applyNotice(CourseNoticeApplyDTO request, Long courseId, Long memberId) {

        // 1. 강좌 조회 (FK용 프록시)
        Course course = courseRepository.getReferenceById(courseId);

        // 2. 작성자 조회 (FK용 프록시)
        Member author = memberRepository.getReferenceById(memberId);

        // 3. 엔티티 생성 후 저장
        CourseNotice notice = CourseNotice.create(course, author,
                request.getCourseNoticeTitle(), request.getCourseNoticeContent());

        courseNoticeRepository.save(notice);
    }

    // ===== 수정 =====

    @Transactional
    public void editNotice(CourseNoticeApplyDTO request, Long noticeId, Long memberId) {

        // 1. 공지사항 조회
        CourseNotice notice = courseNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 공지사항입니다."));

        // 2. 본인이 작성한 공지사항인지 확인
        if (!notice.getAuthor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인이 작성한 공지사항만 수정할 수 있습니다.");
        }

        // 3. 엔티티 수정 메서드 호출
        notice.editNotice(
                request.getCourseNoticeTitle(),
                request.getCourseNoticeContent()
        );
    }

    // ===== 삭제 =====

    @Transactional
    public void deleteNotice(Long noticeId, Long memberId) {

        // 1. 공지사항 조회
        CourseNotice notice = courseNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 공지사항입니다."));

        // 2. 본인이 작성한 공지사항인지 확인
        if (!notice.getAuthor().getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "본인이 작성한 공지사항만 삭제할 수 있습니다.");
        }

        // 3. DB에서 삭제
        courseNoticeRepository.delete(notice);
    }


    public CourseNoticeDetailDTO detailNotice(Long noticeId) {

        CourseNotice notice = courseNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "존재하지 않는 공지사항입니다."));

        CourseNoticeDetailDTO detail = modelMapper.map(notice, CourseNoticeDetailDTO.class);

        if (notice.getCourse() != null) {
            detail.setCourseID(notice.getCourse().getCourseId());
        }

        if (notice.getAuthor() != null) {
            detail.setAuthorName(notice.getAuthor().getName());
        }

        return detail;
    }
}

