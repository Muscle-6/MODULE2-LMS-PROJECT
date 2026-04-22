package com.wanted.ailienlmsprogram.global.security;

import com.wanted.ailienlmsprogram.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*로그인 성공 후 후처리와 역할별 이동 경로를 담당하는 Handler.
*
* 전체 흐름:
* 1. 인증 성공
* 2. 세션 생성/유지 시간 설정
* 3. MemberService에 로그인 성공 후처리 위임
* 4. 역할(Admin/Insstructor/Student)에 따라 각 홈으로 redirect*/
@Component
@RequiredArgsConstructor
public class SecurityLoginSuccessHandler implements AuthenticationSuccessHandler {

    //로그인 성공 후 회원 lastLoginAt, rank 정책을 처리하는 서비스
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(86400);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        memberService.handleLoginSuccess(userDetails.getMember().getMemberId());

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/admin");
            return;
        }

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"))) {
            response.sendRedirect("/instructor");
            return;
        }

        response.sendRedirect("/main");
    }
}
