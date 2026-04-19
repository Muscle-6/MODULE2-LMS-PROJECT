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
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final QnaService qnaService;

    // 답변 목록 조회
    @GetMapping("/instructor/courses/{courseId}/qna")
    public String answerList(@PathVariable Long courseId,  @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,Model model) {

        // 1. 일단 해당 코스의 전체 질문을 가져옵니다.
        List<QnaResponseDTO> qnaList = qnaService.questionByCourse(courseId, status);

        // 2. 💡 [중요] HTML이 기대하는 이름으로 리스트를 분리합니다.
        List<QnaResponseDTO> answered = qnaList.stream()
                .filter(QnaResponseDTO::isAnswered)
                .collect(Collectors.toList());

        List<QnaResponseDTO> unanswered = qnaList.stream()
                .filter(qna -> !qna.isAnswered())
                .collect(Collectors.toList());

        // 3. 💡 [핵심] HTML에 있는 변수명과 똑같이 모델에 담습니다!
        model.addAttribute("answered", answered);
        model.addAttribute("unanswered", unanswered);
        model.addAttribute("courseId", courseId);

        return "answer/find";
    }

    // 답변 상세 조회
    @GetMapping("/instructor/courses/{courseId}/qna/{qnaId}")
    public String answerDetail(@PathVariable Long courseId,
                               @PathVariable Long qnaId,
                               Model model) {

        QnaDetailResponseDTO qna = qnaService.QnaDetail(qnaId);
        List<QnaDetailResponseDTO> replies = qnaService.Replies(qnaId);

        model.addAttribute("qna", qna);
        model.addAttribute("replies", replies);
        model.addAttribute("courseId", courseId);

        return "answer/detail";
    }

    // 답변 작성
    @PostMapping("/instructor/courses/{courseId}/qna/{qnaId}/reply")
    public String answerWrite(@PathVariable Long courseId,
                              @PathVariable Long qnaId,
                              @ModelAttribute QnawriteRequestDTO request,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        qnaService.replyWrite(courseId, memberId, qnaId, request);

        return "redirect:/instructor/courses/" + courseId + "/qna/" + qnaId;
    }

    // 답변 수정 페이지
    @GetMapping("/instructor/courses/{courseId}/qna/{qnaId}/reply/{replyId}/modify")
    public String answerEditPage(@PathVariable Long courseId,
                                 @PathVariable Long qnaId,
                                 @PathVariable Long replyId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {

        QnaModifyResponseDTO responseDTO = qnaService.modifyPage(replyId, userDetails.getMember().getMemberId());

        model.addAttribute("qna", responseDTO);
        model.addAttribute("courseId", courseId);
        model.addAttribute("qnaId", qnaId);

        return "answer/edit";
    }

    // 답변 수정 처리
    @PostMapping("/instructor/courses/{courseId}/qna/{qnaId}/reply/{replyId}/modify")
    public String answerEdit(@PathVariable Long courseId,
                             @PathVariable Long qnaId,
                             @PathVariable Long replyId,
                             @ModelAttribute QnaModifyRequestDTO requestDTO,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 답변은 title 불필요 → 고정값 세팅
        requestDTO.setQnaTitle("REPLY");
        qnaService.modifyQna(replyId, requestDTO, userDetails.getMember().getMemberId());

        return "redirect:/instructor/courses/" + courseId + "/qna/" + qnaId;
    }

    // 답변 삭제
    @PostMapping("/instructor/courses/{courseId}/qna/{qnaId}/reply/{replyId}/delete")
    public String answerDelete(@PathVariable Long courseId,
                               @PathVariable Long qnaId,
                               @PathVariable Long replyId,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        qnaService.deleteQna(replyId, userDetails.getMember().getMemberId());

        return "redirect:/instructor/courses/" + courseId + "/qna/" + qnaId;
    }
}
