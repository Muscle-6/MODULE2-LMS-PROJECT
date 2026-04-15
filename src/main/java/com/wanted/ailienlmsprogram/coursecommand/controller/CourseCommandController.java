package com.wanted.ailienlmsprogram.coursecommand.controller;

import com.wanted.ailienlmsprogram.continent.dto.ContinentAllResponseDTO;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseCommandDTO;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class CourseCommandController {

    private final ContinentService continentService;
    private final CourseCommandService courseCommandService;

    @GetMapping("/instructor/courses/apply")
    public ModelAndView courseApplyPage(@RequestParam(value = "query", defaultValue = "") String query, ModelAndView mv) {

        List<ContinentAllResponseDTO> continents = continentService.findAllContinents(query);
        mv.addObject("continents", continents);
        mv.setViewName("course/apply");

        return mv;
    }

    @PostMapping("instructor/courses/apply")
    public String courseApply(@ModelAttribute CourseCommandDTO request,
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

}
