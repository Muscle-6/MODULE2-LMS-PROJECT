package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminCourseSearchCondition;
import com.wanted.ailienlmsprogram.admin.service.AdminCourseService;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*관리자 강좌 관리 화면 요청을 처리하는 컨트롤러.
*
* - 강좌 목록을 조회
* - 특정 강좌의 상세 정보를 확인한다.
* - 강좌의 공개 상태(DRAFT/PUBLISHED)를 변경할 수 있다.*/
@Controller
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService adminCourseService;

    // 관리자 강좌 목록 화면을 조회한다.
    @GetMapping("/admin/courses")
    public String courseList(@ModelAttribute("condition") AdminCourseSearchCondition condition,
                             Model model) {

        model.addAttribute("courses", adminCourseService.getCourses(condition));
        model.addAttribute("statuses", CourseStatus.values());

        return "admin/course-list";
    }

    // 특정 강좌의 상세 정보를 조회한다.
    @GetMapping("/admin/courses/{courseId}")
    public String courseDetail(@PathVariable Long courseId,
                               @RequestParam(required = false) String query,
                               @RequestParam(required = false) CourseStatus status,
                               Model model) {

        model.addAttribute("course", adminCourseService.getCourseDetail(courseId));
        model.addAttribute("query", query);
        model.addAttribute("status", status);
        return "admin/course-detail";
    }

    // 특정 강좌의 공개 상태를 변경한다.
    @PostMapping("/admin/courses/{courseId}/status")
    public String changeStatus(@PathVariable Long courseId,
                               @RequestParam CourseStatus targetStatus,
                               @RequestParam(required = false) String query,
                               @RequestParam(required = false) CourseStatus status,
                               RedirectAttributes redirectAttributes) {

        try {
            adminCourseService.changeCourseStatus(courseId, targetStatus);
            redirectAttributes.addFlashAttribute("successMessage", "강좌 공개 상태가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (query != null && !query.isBlank()) {
            redirectAttributes.addAttribute("query", query);
        }

        if (status != null) {
            redirectAttributes.addAttribute("status", status);
        }

        return "redirect:/admin/courses";
    }
}