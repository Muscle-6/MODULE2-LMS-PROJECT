package com.wanted.ailienlmsprogram.member.controller;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/student")
    public String studentPage(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (loginMember.getRole() != Member.MemberRole.STUDENT) {
            return "redirect:/";
        }

        model.addAttribute("loginMember", loginMember);
        return "member/student-home";
    }

    @GetMapping("/instructor")
    public String instructorPage(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (loginMember.getRole() != Member.MemberRole.INSTRUCTOR) {
            return "redirect:/";
        }

        model.addAttribute("loginMember", loginMember);
        return "member/instructor-home";
    }

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            return "redirect:/login";
        }

        if (loginMember.getRole() != Member.MemberRole.ADMIN) {
            return "redirect:/";
        }

        model.addAttribute("loginMember", loginMember);
        return "member/admin-home";
    }
}