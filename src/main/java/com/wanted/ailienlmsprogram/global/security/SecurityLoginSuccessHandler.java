package com.wanted.ailienlmsprogram.global.security;

import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SecurityLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember();

        member.setLastLoginAt(LocalDateTime.now());
        memberRepository.save(member);

        if (member.getRole() == Member.MemberRole.ADMIN) {
            response.sendRedirect("/admin");
            return;
        }

        if (member.getRole() == Member.MemberRole.INSTRUCTOR) {
            response.sendRedirect("/instructor");
            return;
        }

        response.sendRedirect("/student");
    }
}