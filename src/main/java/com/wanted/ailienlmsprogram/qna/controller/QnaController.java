package com.wanted.ailienlmsprogram.qna.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.qna.dto.QnaResponseDTO;
import com.wanted.ailienlmsprogram.qna.dto.QnawriteRequestDTO;
import com.wanted.ailienlmsprogram.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student/course/{courseId}/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    // qna 게시판
    @GetMapping
    public String qnaList(@PathVariable Long courseId, Model model) {

        List<QnaResponseDTO> qnaList = qnaService.questionByCourse(courseId);

        model.addAttribute("qnaList", qnaList);
        model.addAttribute("courseId", courseId);

        return "qna/list";

    }

    // qna 작성화면으로 이동
    @GetMapping("/write")
    public String qnaWriteView(@PathVariable Long courseId, Model model) {

        model.addAttribute("courseId", courseId);

        return "qna/write";
    }

    // 사용자가 작성 시 DB에 찍히게
    @PostMapping("/write")
    public String qnaSave(@PathVariable("courseId") Long courseId,
                          @AuthenticationPrincipal CustomUserDetails userDetails,
                          @ModelAttribute QnawriteRequestDTO dto) {

        Long memberId = userDetails.getMember().getMemberId();

        // 서비스에서 '저장'을 시킵니다. (DTO에는 제목, 내용이 담겨있겠죠?)
        qnaService.qnaWrite(courseId, memberId, dto);

        // 저장이 끝났으면 다시 목록으로 돌아갑니다! (Redirect)
        return "redirect:/student/course/" + courseId + "/qna";
    }


}
