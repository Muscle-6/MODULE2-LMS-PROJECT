package com.wanted.ailienlmsprogram.lecture.controller;

import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureResponseDTO;
import com.wanted.ailienlmsprogram.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final CourseCommandService courseCommandService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/instructor/courses/{courseId}/lectures")
    public ModelAndView findMyLectures(ModelAndView mv, @PathVariable Long courseId) {

        CourseFindDTO course = courseCommandService.findCourseById(courseId);
        List<LectureFindDTO> lectures = lectureService.findMyLectures(courseId);

        mv.addObject("lectures",lectures);
        mv.addObject("course",course);
        mv.setViewName("lecture/find");

        return mv;
    }

    // 내 강좌 조회
    @GetMapping("/student/course/{courseId}")
    public String viewMyLecture(@PathVariable("courseId") Long courseId, Model model, Principal principal) {

        CourseDetailResponseDTO course = courseCommandService.courseDetail(courseId);

        List<LectureResponseDTO> lectureList = lectureService.viewMyLecture(courseId);

        boolean isEnrolled = false;
        if(principal != null) {
            isEnrolled = enrollmentService.isStudentEnrolled(principal.getName(), courseId);
        }

        model.addAttribute("course", course);
        model.addAttribute("lectures", lectureList);
        model.addAttribute("isEnrolled", isEnrolled);

        return "course/detail";
    }

}
