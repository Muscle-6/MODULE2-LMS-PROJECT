package com.wanted.ailienlmsprogram.home.controller;

import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ContinentService continentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("continents", continentService.findAllContinents());
        return "home/index";
    }
}