package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminCourseSearchCondition;
import com.wanted.ailienlmsprogram.admin.service.AdminCourseService;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/courses")
public class AdminCourseController {

    private final AdminCourseService adminCourseService;

    @GetMapping
    public String courseList(@ModelAttribute("condition") AdminCourseSearchCondition condition,
                             Model model) {

        model.addAttribute("courses", adminCourseService.getCourses(condition));
        model.addAttribute("statuses", CourseStatus.values());

        return "admin/course-list";
    }

    @PostMapping("/{courseId}/status")
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