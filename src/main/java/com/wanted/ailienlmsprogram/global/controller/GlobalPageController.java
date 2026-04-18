package com.wanted.ailienlmsprogram.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 글로벌 공통 페이지 이동 컨트롤러.
 * Security 에서 redirect 하는 /access-denied 페이지를 연결한다.
 */
@Controller
public class GlobalPageController {

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "common/access-denied";
    }
}