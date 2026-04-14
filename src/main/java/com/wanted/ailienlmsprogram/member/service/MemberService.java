package com.wanted.ailienlmsprogram.member.service;

import com.wanted.ailienlmsprogram.member.dto.SignupRequest;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member();
        member.setLoginId(request.getLoginId());
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setRole(Member.MemberRole.STUDENT);
        member.setAccountStatus(Member.AccountStatus.ACTIVE);

        // 첫 회원가입 등급
        member.setRank(Member.MemberRank.REPTILIAN);

        LocalDateTime now = LocalDateTime.now();
        member.setCreatedAt(now);
        member.setUpdatedAt(now);

        memberRepository.save(member);
    }

    @Transactional
    public void handleLoginSuccess(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();

        // 로그인 시점에는 승급 없음, 강등만 판단
        applyRankPolicy(member, now);

        // 관리자 조회 및 다음 로그인 기준 시점 갱신
        member.setLastLoginAt(now);
        member.setUpdatedAt(now);
    }

    private void applyRankPolicy(Member member, LocalDateTime now) {
        LocalDateTime lastLoginAt = member.getLastLoginAt();

        // 첫 로그인이라면 강등 없이 현재 등급 유지
        if (lastLoginAt == null) {
            return;
        }

        long inactiveDays = Duration.between(lastLoginAt, now).toDays();

        // 7일 이상 미로그인 -> NOVICE
        if (inactiveDays >= 7) {
            member.setRank(Member.MemberRank.NOVICE);
            return;
        }

        // 3일 이상 7일 미만 미로그인 -> MINERVAL
        if (inactiveDays >= 3) {
            member.setRank(Member.MemberRank.MINERVAL);
        }
    }
}