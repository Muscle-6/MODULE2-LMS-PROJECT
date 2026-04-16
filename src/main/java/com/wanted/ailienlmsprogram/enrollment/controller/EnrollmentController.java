package com.wanted.ailienlmsprogram.enrollment.controller;

import com.wanted.ailienlmsprogram.coursecommand.dto.CourseFindResponseDTO;
import com.wanted.ailienlmsprogram.enrollment.dto.EnrollmentResponseDTO;
import com.wanted.ailienlmsprogram.enrollment.service.EnrollmentService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // 수강 중인 강좌 조회
    @GetMapping("/student/enrollments")
    public String findEnrollmentsByMemberId(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {

        // 세션에서 사용자 이름 가져오기
        String realName = customUserDetails.getMember().getName();
        model.addAttribute("studentName" , realName);

        Long studentId = customUserDetails.getMember().getMemberId();
        // 회원은 여러 강좌 수강 가능
        List<EnrollmentResponseDTO> enrollmentList = enrollmentService.enrollmentsByMemberId(studentId);

        model.addAttribute("enrollments", enrollmentList);

        return "student/enrollments";
    }


}
