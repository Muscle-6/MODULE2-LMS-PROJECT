package com.wanted.ailienlmsprogram.community.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class PostController {

    // 오늘 과제: 대륙 상세에서 게시판 전체 보기 클릭 시 진입
    @GetMapping("/continents/{continentId}/posts")
    public ModelAndView findPostListByContinent(@PathVariable Long continentId, ModelAndView mv) {

        // 실제 게시글 조회는 제외하라고 하셨으니, continentId만 확인용으로 보냅니다.
        mv.addObject("continentId", continentId);

        // 뷰의 이름: templates/community/posts.html 을 찾아가도록 설정
        mv.setViewName("community/posts");

        return mv;
    }
}
