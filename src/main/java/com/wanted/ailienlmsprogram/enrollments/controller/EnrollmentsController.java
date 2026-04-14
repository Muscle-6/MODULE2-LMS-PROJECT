package com.wanted.ailienlmsprogram.enrollments.controller;

import com.wanted.ailienlmsprogram.enrollments.service.EnrollmentsService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class EnrollmentsController {

    private final EnrollmentsService enrollmentsService;

    @GetMapping("/student/enrollments")
    public ModelAndView findMyEnrollments(ModelAndView mv, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        enrollmentsService.findMyEnrollments(memberId);

        return mv;
    }

}
