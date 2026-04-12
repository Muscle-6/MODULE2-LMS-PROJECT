package com.wanted.ailienlmsprogram.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/student")
    public String studentPage() {
        return "member/student-home";
    }

    @GetMapping("/instructor")
    public String instructorPage() {
        return "member/instructor-home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "member/admin-home";
    }
}