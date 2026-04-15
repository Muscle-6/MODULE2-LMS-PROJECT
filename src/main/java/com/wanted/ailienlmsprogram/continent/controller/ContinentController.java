package com.wanted.ailienlmsprogram.continent.controller;

import com.wanted.ailienlmsprogram.continent.dto.ContinentAllResponseDTO;
import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import com.wanted.ailienlmsprogram.coursecommand.dto.CourseDetailResponseDTO;
import com.wanted.ailienlmsprogram.coursecommand.service.CourseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/continents")
@RequiredArgsConstructor
public class ContinentController {

    private final ContinentService continentService;
    private final CourseCommandService courseCommandService;

    // 대륙 전체 조회
    @GetMapping
    public String continentList(String query,Model model) {
        List<ContinentAllResponseDTO> continents = continentService.findAllContinents(query);
        model.addAttribute("continents", continents);

        return "home/index";
    }

    // 대륙 상세 조회
    @GetMapping("/{continentId}")
    public String continentDetail(@PathVariable("continentId") Long continentId, Model model) {

        ContinentAllResponseDTO continent = continentService.getContinentDetail(continentId);

        List<CourseDetailResponseDTO> courses = courseCommandService.courseListByContinent(continentId);


        model.addAttribute("continent", continent);
        model.addAttribute("courses" , courses);

        return "continent/detail";
    }



}