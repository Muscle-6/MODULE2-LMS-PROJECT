package com.wanted.ailienlmsprogram.member.controller;

import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final ContinentService continentService;

    public PageController(ContinentService continentService) {
        this.continentService = continentService;
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "common/access-denied";
    }

    @GetMapping("/main")
    public String mainPage(@RequestParam(required = false) String query, Model model){
        model.addAttribute("continents",continentService.findAllContinents(query));
        model.addAttribute("query", query);
        return "home/index";
    }

    @GetMapping("/student")
    public String studentPage() {
        return "student/student-home";
    }

    @GetMapping("/instructor")
    public String instructorPage() {
        return "instructor/instructor-home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin-home";
    }
}