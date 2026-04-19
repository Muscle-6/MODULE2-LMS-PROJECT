package com.wanted.ailienlmsprogram.lectureprogress.controller;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.lecture.dto.LectureWatchDTO;
import com.wanted.ailienlmsprogram.lecture.service.LectureService;
import com.wanted.ailienlmsprogram.lectureprogress.service.LectureProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class LectureProgressController {

    private final LectureProgressService lectureProgressService;
    private final LectureService lectureService;

    // 강의 시청 페이지
    @GetMapping("/student/lectures/{lectureId}")
    public ModelAndView lectureWatch(ModelAndView mv,
                                     @PathVariable Long lectureId,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        LectureWatchDTO lecture = lectureService.findWatchLecture(lectureId);
        boolean isCompleted = lectureProgressService.isCompleted(lectureId, userDetails.getMember().getMemberId());

        mv.addObject("lecture", lecture);
        mv.addObject("isCompleted", isCompleted);
        mv.setViewName("lecture/watch");

        return mv;
    }

    // 수강 완료 처리
    @PostMapping("/student/lectures/{lectureId}/complete")
    public String completeLecture(@PathVariable Long lectureId,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        lectureProgressService.completeLecture(lectureId, memberId);
        Long courseId = lectureProgressService.findCourseId(lectureId);

        return "redirect:/student/course/" + courseId;
    }
}
