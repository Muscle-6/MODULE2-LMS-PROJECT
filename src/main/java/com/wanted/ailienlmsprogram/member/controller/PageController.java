package com.wanted.ailienlmsprogram.member.controller;

import com.wanted.ailienlmsprogram.continent.service.ContinentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*로그인 이후 사용자에게 보여줄 메인/역할별 홈 화면을 연결하는 Controller*/
@Controller
public class PageController {

    private final ContinentService continentService;

    public PageController(ContinentService continentService) {
        this.continentService = continentService;
    }


    /*학생 메인 페이지
    *
    * query 파라미터가 있으면 대륙 검색 결과를,
    * 없으면 전체 대륙 목록을 모델에 담아 home/index로 전달한다.*/

    @GetMapping("/main")
    public String mainPage(@RequestParam(required = false) String query, Model model){
        model.addAttribute("continents",continentService.findAllContinents(query));
        model.addAttribute("query", query);
        return "home/index";
    }

    @GetMapping("/student")
    public String studentPage() {
        return "student/student-home";
    }

    @GetMapping("/instructor")
    public String instructorPage() {
        return "instructor/instructor-home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin-home";
    }
}