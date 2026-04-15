package com.wanted.ailienlmsprogram.continent.controller;

import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/continents")
@RequiredArgsConstructor
public class ContinentController {

    private final ContinentService continentService;
    private final CourseCommandService courseCommandService;

    @GetMapping
    public String continentList(Model model) {
        model.addAttribute("continents", continentService.findAllContinents());
        return "home/index";
    }

    @GetMapping("/{continentId}")
    public String continentDetail(@PathVariable("continentId") Long continentId, Model model) {

        ContinentAllResponseDTO continent = continentService.getContinentDetail(continentId);

        List<CourseDetailResponseDTO> courses = courseCommandService.courseListByContinent(continentId);


        model.addAttribute("continent", continent);
        model.addAttribute("courses" , courses);

        return "continent/detail";
    }
}