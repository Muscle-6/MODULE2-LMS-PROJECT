package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeWriteRequest;
import com.wanted.ailienlmsprogram.admin.service.AdminContinentNoticeService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/continent-notices")
public class AdminContinentNoticeController {

    private static final String NOTICE_CONTINENT_SESSION_KEY = "selectedContinentNoticeContinentId";

    private final AdminContinentNoticeService adminContinentNoticeService;

    @GetMapping("/continents")
    public String continentList(Model model, HttpSession session) {
        session.removeAttribute(NOTICE_CONTINENT_SESSION_KEY);
        model.addAttribute("continents", adminContinentNoticeService.getContinents());
        return "admin/continent-notice-continent-list";
    }

    @PostMapping("/select")
    public String selectContinent(@RequestParam Long continentId, HttpSession session) {
        session.setAttribute(NOTICE_CONTINENT_SESSION_KEY, continentId);
        return "redirect:/admin/continent-notices/write";
    }

    @GetMapping("/write")
    public String writePage(Model model, HttpSession session) {
        Long continentId = (Long) session.getAttribute(NOTICE_CONTINENT_SESSION_KEY);

        if (continentId == null) {
            return "redirect:/admin/continent-notices/continents";
        }

        model.addAttribute("continent", adminContinentNoticeService.getWritePage(continentId));

        if (!model.containsAttribute("request")) {
            AdminContinentNoticeWriteRequest request = new AdminContinentNoticeWriteRequest();
            request.setContinentId(continentId);
            model.addAttribute("request", request);
        }

        return "admin/continent-notice-write";
    }

    @PostMapping
    public String writeNotice(@Valid @ModelAttribute("request") AdminContinentNoticeWriteRequest request,
                              BindingResult bindingResult,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        Long continentId = (Long) session.getAttribute(NOTICE_CONTINENT_SESSION_KEY);

        if (continentId == null) {
            return "redirect:/admin/continent-notices/continents";
        }

        request.setContinentId(continentId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("continent", adminContinentNoticeService.getWritePage(continentId));
            return "admin/continent-notice-write";
        }

        try {
            adminContinentNoticeService.writeNotice(userDetails.getMember().getMemberId(), request);
            redirectAttributes.addFlashAttribute("successMessage", "대륙 공지사항이 등록되었습니다.");
            return "redirect:/admin/continent-notices/write";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("continent", adminContinentNoticeService.getWritePage(continentId));
            return "admin/continent-notice-write";
        }
    }
}