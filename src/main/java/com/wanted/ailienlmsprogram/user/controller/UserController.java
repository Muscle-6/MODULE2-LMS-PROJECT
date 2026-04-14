package com.wanted.ailienlmsprogram.user.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.user.dto.UserDTO;
import com.wanted.ailienlmsprogram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/student/profile")
    public ModelAndView studentProfile(ModelAndView mv,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        UserDTO user = userService.findUserById(memberId);  // memberId 로 조회

        mv.addObject("user", user);
        mv.setViewName("student/profile");
        return mv;
    }

    @GetMapping("/instructor/profile")
    public ModelAndView instructorProfile(ModelAndView mv,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        UserDTO user = userService.findUserById(memberId);  // memberId 로 조회

        mv.addObject("user", user);
        mv.setViewName("instructor/profile");
        return mv;
    }
}