package com.wanted.ailienlmsprogram.qna.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.qna.dto.*;
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
    public String qnaList(@PathVariable Long courseId,
                          @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
                          Model model) {

        // 1. 서비스에서 필터링된 리스트를 가져옵니다.
        List<QnaResponseDTO> qnaList = qnaService.questionByCourse(courseId, status);

        // 2. 뷰에서 버튼 활성화 상태를 제어하기 위해 status를 넘겨줍니다.
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("courseId", courseId);
        model.addAttribute("currentStatus", status); // 💡 HTML의 th:classappend에서 사용!

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

    @PostMapping("/{qnaId}/delete")
    public String qnaDelete(@PathVariable Long qnaId,
                            @PathVariable Long courseId,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        qnaService.deleteQna(qnaId, userDetails.getMember().getMemberId());

        return "redirect:/student/course/" + courseId + "/qna";
    }


    // qna 상세조회
    @GetMapping("/{qnaId}")
    public String qnaDetail(@PathVariable Long courseId,
                            @PathVariable Long qnaId,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            Model model) {

        QnaDetailResponseDTO qna = qnaService.QnaDetail(qnaId);
        List<QnaDetailResponseDTO> replies = qnaService.Replies(qnaId);

        model.addAttribute("qna", qna);
        model.addAttribute("replies", replies);
        model.addAttribute("courseId", courseId);
        model.addAttribute("currentMemberId", userDetails.getMember().getMemberId());

        return "qna/detail";
    }

    // 페이지로 이동
    @GetMapping("/{qnaId}/modify")
    public String modifyPage(@PathVariable Long courseId,
                           @PathVariable Long qnaId,
                           @AuthenticationPrincipal CustomUserDetails userDetails,
                           Model model) {

        QnaModifyResponseDTO responseDTO = qnaService.modifyPage(qnaId, userDetails.getMember().getMemberId());

        model.addAttribute("qna", responseDTO);
        model.addAttribute("courseId", courseId);

        return "qna/modify";
    }

    @PostMapping("/{qnaId}/modify")
    public String modifyQna(@PathVariable Long courseId,
                            @PathVariable Long qnaId,
                            QnaModifyRequestDTO requsetDTO,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        qnaService.modifyQna(qnaId, requsetDTO, userDetails.getMember().getMemberId());

        return "redirect:/student/course/" + courseId + "/qna/" + qnaId;
    }

}
