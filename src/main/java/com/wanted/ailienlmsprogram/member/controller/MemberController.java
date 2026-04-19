package com.wanted.ailienlmsprogram.member.controller;

import com.wanted.ailienlmsprogram.member.dto.SignupRequest;
import com.wanted.ailienlmsprogram.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/*회원가입 및 로그인 페이지 진입을 담당하는 컨트롤러.*/
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // GET/login은 로그인 화면만 반환한다.
    /*실제 로그인 인증 처리는 Spring Security의 formLogin 설정이 담당한다.*/
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    //회원가입 페이지 진입
    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("request",new SignupRequest());
        return "member/signup";
    }

    //회원가입 요청 처리.
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("request") SignupRequest request,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "member/signup";
        }

        try {
            memberService.signup(request);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/signup";
        }
    }
}