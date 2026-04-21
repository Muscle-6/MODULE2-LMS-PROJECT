package com.wanted.ailienlmsprogram.coursenotice.controller;

import com.wanted.ailienlmsprogram.coursenotice.dto.CourseNoticeApplyDTO;
import com.wanted.ailienlmsprogram.coursenotice.dto.CourseNoticeDetailDTO;
import com.wanted.ailienlmsprogram.coursenotice.dto.CourseNoticeFindDTO;
import com.wanted.ailienlmsprogram.coursenotice.service.CourseNoticeService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseNoticeController {

    private final CourseNoticeService courseNoticeService;

    // ===== 조회 =====
    @GetMapping("/instructor/courses/{courseId}/notices")
    public ModelAndView findNotices(ModelAndView mv,
                                    @PathVariable Long courseId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();

        List<CourseNoticeFindDTO> notices = courseNoticeService.findNotices(courseId, memberId);
        mv.addObject("notices", notices);
        mv.addObject("courseId", courseId);
        mv.setViewName("lecturenotice/find");

        return mv;
    }

    // ===== 등록 폼 =====
    @GetMapping("/instructor/courses/{courseId}/notices/add")
    public ModelAndView addNoticePage(ModelAndView mv,
                                      @PathVariable Long courseId) {

        mv.addObject("courseId", courseId);
        mv.setViewName("lecturenotice/apply");

        return mv;
    }

    // ===== 등록 처리 =====
    @PostMapping("/instructor/courses/{courseId}/notices/add")
    public String applyNotice(@ModelAttribute CourseNoticeApplyDTO request,
                              @PathVariable Long courseId,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        courseNoticeService.applyNotice(request, courseId, memberId);

        return "redirect:/instructor/courses/" + courseId + "/notices";
    }

    // ===== 수정 폼 =====
    @GetMapping("/instructor/courses/{courseId}/notices/{noticeId}/edit")
    public ModelAndView editNoticePage(ModelAndView mv,
                                       @PathVariable Long courseId,
                                       @PathVariable Long noticeId) {

        CourseNoticeFindDTO notice = courseNoticeService.findNoticeById(noticeId);
        mv.addObject("notice", notice);
        mv.addObject("courseId", courseId);
        mv.setViewName("lecturenotice/edit");

        return mv;
    }

    // ===== 수정 처리 =====
    @PostMapping("/instructor/courses/{courseId}/notices/{noticeId}/edit")
    public String editNotice(@ModelAttribute CourseNoticeApplyDTO request,
                             @PathVariable Long courseId,
                             @PathVariable Long noticeId,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        courseNoticeService.editNotice(request, noticeId, memberId);

        return "redirect:/instructor/courses/" + courseId + "/notices";
    }

    // ===== 삭제 확인 페이지 =====
    @GetMapping("/instructor/courses/{courseId}/notices/{noticeId}/delete")
    public ModelAndView deleteNoticePage(ModelAndView mv,
                                         @PathVariable Long courseId,
                                         @PathVariable Long noticeId) {

        CourseNoticeFindDTO notice = courseNoticeService.findNoticeById(noticeId);
        mv.addObject("notice", notice);
        mv.addObject("courseId", courseId);
        mv.setViewName("lecturenotice/delete");

        return mv;
    }

    // ===== 삭제 처리 =====
    @PostMapping("/instructor/courses/{courseId}/notices/{noticeId}/delete")
    public String deleteNotice(@PathVariable Long courseId,
                               @PathVariable Long noticeId,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();
        courseNoticeService.deleteNotice(noticeId, memberId);

        return "redirect:/instructor/courses/" + courseId + "/notices";
    }

    // 학생이 공지사항 조회
    @GetMapping("/student/courses/{courseId}/notices")
    public ModelAndView findStudentNotices(ModelAndView mv, @PathVariable Long courseId, @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getMember().getMemberId();

        try {
            List<CourseNoticeFindDTO> notices = courseNoticeService.findNotices(courseId, memberId);
            mv.addObject("notices", notices);
            mv.addObject("accessDenied", false);
        } catch (AccessDeniedException e) {
            mv.addObject("notices", List.of());
            mv.addObject("accessDenied", true);
        }

        mv.addObject("courseId", courseId);
        mv.setViewName("student/notice-list");

        return mv;
    }

    // 공지 상세 조회
    @GetMapping("/student/notice/{noticeId}")
    public String findNoticeDetail(@PathVariable("noticeId") Long noticeId, Model model) {
        CourseNoticeDetailDTO notice = courseNoticeService.detailNotice(noticeId);
        model.addAttribute("notice", notice);

        model.addAttribute("courseId", notice.getCourseID());

        return "student/notice-detail";
    }
}

