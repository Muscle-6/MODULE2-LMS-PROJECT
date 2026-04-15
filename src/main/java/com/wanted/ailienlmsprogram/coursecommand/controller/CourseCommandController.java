package com.wanted.ailienlmsprogram.coursecommand.controller;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseCommandDTO;
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

    @GetMapping("/instructor/courses/apply")
    public ModelAndView courseApplyPage(ModelAndView mv) {

        List<Continent> continents = continentService.findAllContinents();
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

}
