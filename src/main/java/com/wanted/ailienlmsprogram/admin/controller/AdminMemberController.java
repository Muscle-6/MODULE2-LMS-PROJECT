package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminMemberSearchCondition;
import com.wanted.ailienlmsprogram.admin.service.AdminMemberService;
import com.wanted.ailienlmsprogram.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*관리자 회원 관리 기능을 담당하는 컨트롤러
*
* - 회원 목록 및 검색 결과를 조회한다.
* - 회원 상태를 변경한다.
* - 회원 삭제 요청을 처리한다.
* - 처리 후 기존 검색 조건을 유지한 채 목록 화면으로 리다이렉트한다.*/
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    //회원 목록 화면을 조회한다.
    @GetMapping
    public String memberList(@ModelAttribute("condition") AdminMemberSearchCondition condition,
                             Model model) {
        model.addAttribute("members", adminMemberService.getMembers(condition));
        model.addAttribute("roles", Member.MemberRole.values());
        return "admin/member-list";
    }

    //특정 회원의 상태를 토글한다.
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

    //특정 회원을 삭제처리한다.
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