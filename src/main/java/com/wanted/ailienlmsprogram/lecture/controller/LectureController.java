package com.wanted.ailienlmsprogram.lecture.controller;

import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.lecture.dto.LectureAddDTO;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.service.LectureService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;
    private final CourseCommandService courseCommandService;

    @GetMapping("/instructor/courses/{courseId}/lectures")
    public ModelAndView findMyLectures(ModelAndView mv, @PathVariable Long courseId) {

        CourseFindDTO course = courseCommandService.findCourseById(courseId);
        List<LectureFindDTO> lectures = lectureService.findMyLectures(courseId);

        mv.addObject("lectures",lectures);
        mv.addObject("course",course);
        mv.setViewName("lecture/find");

        return mv;
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
}
