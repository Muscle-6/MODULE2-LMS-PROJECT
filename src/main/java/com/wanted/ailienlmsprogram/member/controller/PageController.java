package com.wanted.ailienlmsprogram.member.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/student")
    public String studentPage(HttpSession session) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/login";
        }
        return "member/student-home";
    }

    @GetMapping("/instructor")
    public String instructorPage(HttpSession session) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/login";
        }
        return "member/instructor-home";
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/login";
        }
        return "member/admin-home";
    }
}
