package com.wanted.ailienlmsprogram.admin.controller;

import com.wanted.ailienlmsprogram.admin.dto.AdminInstructorCreateRequest;
import com.wanted.ailienlmsprogram.admin.service.AdminInstructorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/*관리자 강사 계쩡 생성 기능을 담당하는 컨트롤러
*
* - 강사 계정 생성 화면을 조회한다.
* - 강사 계정 생성 요청을 받아 서비스에 위임한다.
* - 입력값 검증 실패 또는 중복 데이터 예외 발생 시 생성 폼으로 다시 이동시킨다.*/
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/instructors")
public class AdminInstructorController {

    private final AdminInstructorService adminInstructorService;

    //강사 계정 생성 페이지를 조회한다.
    @GetMapping("/new")
    public String newInstructorPage(Model model) {
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new AdminInstructorCreateRequest());
        }
        return "admin/instructor-create";
    }

    //강사 계정 생성 요청을 처리한다.
    @PostMapping("/new")
    public String createInstructor(
            @Valid @ModelAttribute("request") AdminInstructorCreateRequest request,
            BindingResult bindingResult,
            Model model
    ) {
        // 검증 오류가 있으면 생성 페이지로 다시 이동한다.
        if (bindingResult.hasErrors()) {
            return "admin/instructor-create";
        }
        //정상 입력이면 서비스에 강사 계정 생성을 위임한다.
        //중복 아이디/이메일 등 비즈니스 예외 발생 시 에러 메시지를 모델에 담아 반환한다.
        try {
            adminInstructorService.createInstructor(request);
            return "redirect:/admin?createdInstructor=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/instructor-create";
        }
    }
}