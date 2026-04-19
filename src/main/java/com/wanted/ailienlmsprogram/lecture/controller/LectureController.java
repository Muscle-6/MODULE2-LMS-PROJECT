package com.wanted.ailienlmsprogram.lecture.controller;

import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.lecture.dto.LectureAddDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureResponseDTO;
import com.wanted.ailienlmsprogram.lecture.service.LectureService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        Long enrollmentId = null;  // 추가

        if (principal != null) {
            isEnrolled = enrollmentService.isStudentEnrolled(principal.getName(), courseId);

            if (isEnrolled) {
                // enrollmentId 가져오기 — EnrollmentService에 메서드 추가 필요
                enrollmentId = enrollmentService.findEnrollmentId(principal.getName(), courseId);

                // 진척도 가져오기
                BigDecimal progressRate = enrollmentService.getProgressRate(principal.getName(), courseId);
                model.addAttribute("progressRate", progressRate);
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("lectures", lectureList);
        model.addAttribute("isEnrolled", isEnrolled);
        model.addAttribute("enrollmentId", enrollmentId);  // 추가

        return "course/detail";
    }
    @GetMapping("/instructor/courses/{courseId}/lectures/add")
    public String addMyLecturePage(@PathVariable Long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "lecture/add";
    }

    @PostMapping("/instructor/courses/{courseId}/lectures/add")
    public String applyMyLecture(@ModelAttribute LectureAddDTO request,
                              @RequestParam(value = "lectureVideo", required = false) MultipartFile lectureVideo,
                              @PathVariable Long courseId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        Long memberId = userDetails.getMember().getMemberId();
        lectureService.applyLecture(request, courseId, memberId, lectureVideo);

        return "redirect:/instructor/courses/" + courseId + "/lectures";
    }

    @GetMapping("/instructor/courses/{courseId}/lectures/{lectureId}/edit")
    public ModelAndView editMyCoursePage(ModelAndView mv,
                                         @PathVariable Long courseId,
                                         @PathVariable Long lectureId) {

        LectureFindDTO lecture = lectureService.findLectureById(lectureId);
        mv.addObject("lecture", lecture);
        mv.addObject("courseId", courseId);
        mv.setViewName("lecture/edit");

        return mv;
    }

    @PostMapping("/instructor/courses/{courseId}/lectures/{lectureId}/edit")
    public String editMyLecture(@ModelAttribute LectureAddDTO request,
                                 @RequestParam(value = "lectureVideo", required = false) MultipartFile lectureVideo,
                                 @PathVariable Long courseId,
                                 @PathVariable Long lectureId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        Long memberId = userDetails.getMember().getMemberId();
        lectureService.editLecture(request, lectureId, memberId, lectureVideo);

        return "redirect:/instructor/courses/" + courseId + "/lectures";
    }

    @GetMapping("/instructor/courses/{courseId}/lectures/{lectureId}/delete")
    public String deleteLecture(@PathVariable Long courseId,
                                @PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);

        return "redirect:/instructor/courses/" + courseId + "/lectures";
    }

}
