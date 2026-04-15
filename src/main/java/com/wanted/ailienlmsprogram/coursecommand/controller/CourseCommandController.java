package com.wanted.ailienlmsprogram.coursecommand.controller;

import com.wanted.ailienlmsprogram.continent.dto.ContinentAllResponseDTO;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseCommandDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseApplyDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseEditDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class CourseCommandController {

    private final ContinentService continentService;
    private final CourseCommandService courseCommandService;

    // 강좌 등록 페이지로 이동
    @GetMapping("/instructor/courses/apply")
    public ModelAndView courseApplyPage(@RequestParam(value = "query", defaultValue = "") String query, ModelAndView mv) {

        List<ContinentAllResponseDTO> continents = continentService.findAllContinents(query);
        mv.addObject("continents", continents);
        mv.setViewName("course/apply");

        return mv;
    }

    // 강좌 등록 폼에 작성된 내용을 DB에 INSERT
    @PostMapping("instructor/courses/apply")
    public String courseApply(@ModelAttribute CourseApplyDTO request,
                              @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
                              @AuthenticationPrincipal
                              CustomUserDetails userDetails) throws IOException {

        Long memberId = userDetails.getMember().getMemberId();
        courseCommandService.applyCourse(request,memberId,thumbnailFile);

        return "redirect:/instructor";
    }

    // 강좌 세부 조회
    @GetMapping("/courses/{courseId}")
    public String courseDetail(@PathVariable("courseId") Long courseId, Model model) {

        CourseDetailResponseDTO course = courseCommandService.courseDetail(courseId);

        model.addAttribute("course" ,course);

        return "course/detail";
    }

    // 내 강좌 조회 기능
    @GetMapping("/instructor/courses/{courseStatus}")
    public ModelAndView findMyCourses(ModelAndView mv,
                                      @PathVariable CourseStatus courseStatus,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getMemberId();
        List<CourseFindDTO> courses = courseCommandService.findMyCourses(memberId,courseStatus);

        mv.addObject("courses", courses);
        mv.addObject("currentStatus", courseStatus);
        mv.setViewName("course/find");

        return mv;
    }

    @GetMapping("/instructor/courses/{courseId}/edit")
    public ModelAndView editMyCourse(ModelAndView mv,
                                     @RequestParam(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,
                                     @PathVariable Long courseId,
                                     @ModelAttribute CourseApplyDTO request) throws IOException {

        CourseEditDTO course = courseCommandService.editMyCourse(thumbnailFile, courseId,request);
        mv.addObject("editCourse", course);
        mv.setViewName("/course/edit");

        return mv;
    }

}
