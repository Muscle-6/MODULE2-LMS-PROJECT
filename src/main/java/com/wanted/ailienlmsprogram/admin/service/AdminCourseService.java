package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminCourseDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminCourseListResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminCourseSearchCondition;
import com.wanted.ailienlmsprogram.admin.repository.AdminCourseQueryRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*관리자 강좌 관리 비즈니스 로직을 담당하는 서비스
* - 강좌 목록 조회, 강좌 상세 조회, 강좌 공개 상태 변경과 같이
* - 관리자 강좌 관리 기능의 핵심 흐름을 처리한다.*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCourseService {

    private final AdminCourseQueryRepository adminCourseQueryRepository;

    // 검색 조건에 맞는 강좌 목록을 조회한다.
    @Transactional(readOnly = true)
    public List<AdminCourseListResponse> getCourses(AdminCourseSearchCondition condition) {
        return adminCourseQueryRepository.findCourses(condition);
    }

    //특정 강좌의 상세 정보를 조회한다.
    @Transactional(readOnly = true)
    public AdminCourseDetailResponse getCourseDetail(Long courseId) {
        AdminCourseDetailResponse detail = adminCourseQueryRepository.findCourseDetail(courseId);

        if (detail == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "강좌가 존재하지 않습니다.");
        }

        return detail;
    }

    @Transactional
    public void changeCourseStatus(Long courseId, CourseStatus targetStatus) {
        Course course = getTarget(courseId);

        if (targetStatus == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "변경할 강좌 상태가 없습니다.");
        }

        if (course.getCourseStatus() == targetStatus) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 해당 상태로 설정된 강좌입니다.");
        }

        int updatedCount = adminCourseQueryRepository.updateCourseStatus(courseId, targetStatus);

        if (updatedCount == 0) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "강좌 상태 변경에 실패했습니다.");
        }
    }

    //상태 변경 대상 강좌 엔티티를 조회한다.
    private Course getTarget(Long courseId) {
        Course course = adminCourseQueryRepository.findCourse(courseId);

        if (course == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "강좌가 존재하지 않습니다.");
        }

        return course;
    }
}