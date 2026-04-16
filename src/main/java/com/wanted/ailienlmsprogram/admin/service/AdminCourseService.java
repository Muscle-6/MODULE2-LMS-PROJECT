package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminCourseDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminCourseListResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminCourseSearchCondition;
import com.wanted.ailienlmsprogram.admin.repository.AdminCourseQueryRepository;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCourseService {

    private final AdminCourseQueryRepository adminCourseQueryRepository;

    @Transactional(readOnly = true)
    public List<AdminCourseListResponse> getCourses(AdminCourseSearchCondition condition) {
        return adminCourseQueryRepository.findCourses(condition);
    }

    @Transactional(readOnly = true)
    public AdminCourseDetailResponse getCourseDetail(Long courseId) {
        AdminCourseDetailResponse detail = adminCourseQueryRepository.findCourseDetail(courseId);

        if (detail == null) {
            throw new IllegalArgumentException("강좌가 존재하지 않습니다.");
        }

        return detail;
    }

    public void changeCourseStatus(Long courseId, CourseStatus targetStatus) {
        Course course = getTarget(courseId);

        if (targetStatus == null) {
            throw new IllegalArgumentException("변경할 강좌 상태가 없습니다.");
        }

        if (course.getCourseStatus() == targetStatus) {
            throw new IllegalArgumentException("이미 해당 상태로 설정된 강좌입니다.");
        }

        int updatedCount = adminCourseQueryRepository.updateCourseStatus(courseId, targetStatus);

        if (updatedCount == 0) {
            throw new IllegalArgumentException("강좌 상태 변경에 실패했습니다.");
        }
    }

    private Course getTarget(Long courseId) {
        Course course = adminCourseQueryRepository.findCourse(courseId);

        if (course == null) {
            throw new IllegalArgumentException("강좌가 존재하지 않습니다.");
        }

        return course;
    }
}