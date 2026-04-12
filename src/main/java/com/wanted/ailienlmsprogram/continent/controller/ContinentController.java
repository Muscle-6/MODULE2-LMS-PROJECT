package com.wanted.ailienlmsprogram.continent.controller;

import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/continents")
@RequiredArgsConstructor
public class ContinentController {

    private final ContinentService continentService;

    @GetMapping("/{continentId}")
    public String detail(@PathVariable Long continentId, Model model) {
        model.addAttribute("continent", continentService.findById(continentId));
        return "continent/detail";
    }
}