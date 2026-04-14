package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminInstructorCreateRequest;
import com.wanted.ailienlmsprogram.admin.service.AdminInstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/instructors")
public class AdminInstructorController {

    private final AdminInstructorService adminInstructorService;

    @GetMapping("/new")
    public String newInstructorPage(Model model) {
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new AdminInstructorCreateRequest());
        }
        return "admin/instructor-create";
    }

    @PostMapping("/new")
    public String createInstructor(
            @Valid @ModelAttribute("request") AdminInstructorCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/instructor-create";
        }

        try {
            adminInstructorService.createInstructor(request);
            return "redirect:/admin?createdInstructor=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/instructor-create";
        }
    }
}