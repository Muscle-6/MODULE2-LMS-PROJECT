package com.wanted.ailienlmsprogram.coursecommand.controller;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseApplyDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindDTO;
import com.wanted.ailienlmsprogram.coursecommand.entity.CourseStatus;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public ModelAndView courseApplyPage(ModelAndView mv) {

        List<Continent> continents = continentService.findAllContinents();
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

    // 내 강좌 조회 기능
    @GetMapping("/instructor/courses")
    public ModelAndView findMyCourses(ModelAndView mv,
                                      @RequestParam CourseStatus courseStatus,
                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        Long memberId = userDetails.getMember().getMemberId();
        List<CourseFindDTO> courses = courseCommandService.findMyCourses(memberId,courseStatus);

        mv.addObject("courses", courses);
        mv.addObject("currentStatus", courseStatus);
        mv.setViewName("course/find");

        return mv;
    }

}
