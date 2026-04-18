package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeFormRequest;
import com.wanted.ailienlmsprogram.admin.service.AdminContinentNoticeService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/continent-notices")
public class AdminContinentNoticeController {

    private final AdminContinentNoticeService adminContinentNoticeService;

    /**
     * 관리자 홈 -> 대륙 선택 화면
     * 또는 특정 대륙의 공지 목록 화면
     */
    @GetMapping
    public String entry(@RequestParam(required = false) Long continentId, Model model) {
        if (continentId == null) {
            model.addAttribute("continents", adminContinentNoticeService.getContinents());
            return "admin/continent-notice-continent-list";
        }

        loadNoticeList(continentId, model);
        return "admin/continent-notice-list";
    }

    /**
     * 공지 등록 폼 조회
     */
    @GetMapping("/new")
    public String createForm(@RequestParam Long continentId, Model model) {
        AdminContinentNoticeFormRequest form = new AdminContinentNoticeFormRequest();
        form.setContinentId(continentId);

        model.addAttribute("form", form);
        model.addAttribute("mode", "create");
        model.addAttribute("continent", adminContinentNoticeService.getContinent(continentId));
        return "admin/continent-notice-form";
    }

    /**
     * 공지 등록 처리
     */
    @PostMapping
    public String create(@Valid @ModelAttribute("form") AdminContinentNoticeFormRequest form,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("continent", adminContinentNoticeService.getContinent(form.getContinentId()));
            return "admin/continent-notice-form";
        }

        try {
            adminContinentNoticeService.createNotice(form, userDetails.getMember().getMemberId());
            redirectAttributes.addAttribute("continentId", form.getContinentId());
            redirectAttributes.addFlashAttribute("successMessage", "공지글이 등록되었습니다.");
            return "redirect:/admin/continent-notices";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mode", "create");
            model.addAttribute("continent", adminContinentNoticeService.getContinent(form.getContinentId()));
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/continent-notice-form";
        }
    }

    /**
     * 공지 수정 폼 조회
     */
    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId,
                           @RequestParam Long continentId,
                           Model model) {

        AdminContinentNoticeDetailResponse detail =
                adminContinentNoticeService.getNoticeDetail(continentId, postId);

        AdminContinentNoticeFormRequest form = new AdminContinentNoticeFormRequest();
        form.setPostId(detail.getPostId());
        form.setContinentId(detail.getContinentId());
        form.setPostTitle(detail.getPostTitle());
        form.setPostContent(detail.getPostContent());

        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("continent", adminContinentNoticeService.getContinent(continentId));
        return "admin/continent-notice-form";
    }

    /**
     * 공지 수정 처리
     */
    @PostMapping("/{postId}")
    public String update(@PathVariable Long postId,
                         @Valid @ModelAttribute("form") AdminContinentNoticeFormRequest form,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        form.setPostId(postId);

        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("continent", adminContinentNoticeService.getContinent(form.getContinentId()));
            return "admin/continent-notice-form";
        }

        try {
            adminContinentNoticeService.updateNotice(form);
            redirectAttributes.addAttribute("continentId", form.getContinentId());
            redirectAttributes.addFlashAttribute("successMessage", "공지글이 수정되었습니다.");
            return "redirect:/admin/continent-notices";
        } catch (IllegalArgumentException e) {
            model.addAttribute("mode", "edit");
            model.addAttribute("continent", adminContinentNoticeService.getContinent(form.getContinentId()));
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/continent-notice-form";
        }
    }

    /**
     * 공지 삭제 처리
     */
    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId,
                         @RequestParam Long continentId,
                         RedirectAttributes redirectAttributes) {

        try {
            adminContinentNoticeService.deleteNotice(continentId, postId);
            redirectAttributes.addFlashAttribute("successMessage", "공지글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        redirectAttributes.addAttribute("continentId", continentId);
        return "redirect:/admin/continent-notices";
    }

    private void loadNoticeList(Long continentId, Model model) {
        model.addAttribute("continent", adminContinentNoticeService.getContinent(continentId));
        model.addAttribute("notices", adminContinentNoticeService.getNoticePosts(continentId));
    }
}