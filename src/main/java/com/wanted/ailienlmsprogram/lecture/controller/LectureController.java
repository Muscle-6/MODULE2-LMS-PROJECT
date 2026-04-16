package com.wanted.ailienlmsprogram.lecture.controller;

import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.lecture.dto.LectureFindDTO;
import com.wanted.ailienlmsprogram.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

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



}
