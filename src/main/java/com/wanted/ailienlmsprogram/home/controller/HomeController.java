package com.wanted.ailienlmsprogram.home.controller;

import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ContinentService continentService;

    @GetMapping({"/", "/main"})
    public String mainPage(Model model, HttpSession session) {
        model.addAttribute("continents", continentService.findAllContinents());

        Member loginMember = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginMember", loginMember);

        return "home/index";
    }
}