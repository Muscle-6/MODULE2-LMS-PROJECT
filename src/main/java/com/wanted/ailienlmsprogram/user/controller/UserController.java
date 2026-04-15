package com.wanted.ailienlmsprogram.user.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.user.dto.UserFindDTO;
import com.wanted.ailienlmsprogram.user.dto.UserEditDTO;
import com.wanted.ailienlmsprogram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 학생 내 정보 조회
    @GetMapping("/student/profile")
    public ModelAndView studentProfile(ModelAndView mv,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        UserFindDTO user = userService.findUserById(memberId);

        mv.addObject("user", user);
        mv.setViewName("student/profile");
        return mv;
    }

    // 학생 내 정보 수정 페이지
    @GetMapping("/student/profile/edit")
    public ModelAndView editStudentProfilePage(ModelAndView mv,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        UserFindDTO user = userService.findUserById(memberId);

        mv.addObject("user", user);
        mv.setViewName("student/edit");
        return mv;
    }

    // 학생 내 정보 수정 처리
    @PostMapping("/student/profile/edit")
    public String editStudentProfile(
            @ModelAttribute UserEditDTO request,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        Long memberId = userDetails.getMember().getMemberId();
        userService.editUserProfile(memberId, request, profileImageFile);

        return "redirect:/student/profile";
    }

    // 강사 내 정보 조회
    @GetMapping("/instructor/profile")
    public ModelAndView findInstructorProfile(ModelAndView mv,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        UserFindDTO user = userService.findUserById(memberId);

        mv.addObject("user", user);
        mv.setViewName("instructor/profile");
        return mv;
    }

    // 강사 내 정보 수정 페이지
    @GetMapping("/instructor/profile/edit")
    public ModelAndView editInstructorProfilePage(ModelAndView mv,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        UserFindDTO user = userService.findUserById(memberId);

        mv.addObject("user", user);
        mv.setViewName("instructor/edit");
        return mv;
    }

    // 강사 내 정보 수정 처리
    @PostMapping("/instructor/profile/edit")
    public String editInstructorProfile(
            @ModelAttribute UserEditDTO request,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        Long memberId = userDetails.getMember().getMemberId();
        userService.editUserProfile(memberId, request, profileImageFile);

        return "redirect:/instructor/profile";
    }

}