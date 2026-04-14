package com.wanted.ailienlmsprogram.global.common;

import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthMemberAdvice {

    private final MemberRepository memberRepository;

    @ModelAttribute("loginMember")
    public Member loginMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }

        Long memberId = userDetails.getMember().getMemberId();

        // 최신 DB 값 기준으로 화면 표시용 loginMember 주입
        return memberRepository.findById(memberId).orElse(null);
    }
}