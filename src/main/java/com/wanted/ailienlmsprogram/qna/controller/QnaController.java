package com.wanted.ailienlmsprogram.qna.controller;

import com.wanted.ailienlmsprogram.qna.dto.QnaResponseDTO;
import com.wanted.ailienlmsprogram.qna.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
