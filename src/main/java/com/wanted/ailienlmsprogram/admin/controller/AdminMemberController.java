package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminMemberSearchCondition;
import com.wanted.ailienlmsprogram.admin.service.AdminMemberService;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public String memberList(@ModelAttribute("condition") AdminMemberSearchCondition condition,
                             Model model) {
        model.addAttribute("members", adminMemberService.getMembers(condition));
        model.addAttribute("roles", Member.MemberRole.values());
        return "admin/member-list";
    }

    @PostMapping("/{memberId}/toggle-status")
    public String toggleStatus(@PathVariable Long memberId,
                               @RequestParam(required = false) String query,
                               @RequestParam(required = false) Member.MemberRole role,
                               RedirectAttributes redirectAttributes) {
        try {
            adminMemberService.toggleMemberStatus(memberId);
            redirectAttributes.addFlashAttribute("successMessage", "회원 상태가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (query != null && !query.isBlank()) {
            redirectAttributes.addAttribute("query", query);
        }
        if (role != null) {
            redirectAttributes.addAttribute("role", role);
        }

        return "redirect:/admin/members";
    }

    @PostMapping("/{memberId}/delete")
    public String delete(@PathVariable Long memberId,
                         @RequestParam(required = false) String query,
                         @RequestParam(required = false) Member.MemberRole role,
                         RedirectAttributes redirectAttributes) {
        try {
            adminMemberService.deleteMember(memberId);
            redirectAttributes.addFlashAttribute("successMessage", "회원이 삭제 처리되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        if (query != null && !query.isBlank()) {
            redirectAttributes.addAttribute("query", query);
        }
        if (role != null) {
            redirectAttributes.addAttribute("role", role);
        }

        return "redirect:/admin/members";
    }
}